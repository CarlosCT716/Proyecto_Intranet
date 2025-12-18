package com.cibertec.intranet.asistente.service;

import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.academico.model.Horario;
import com.cibertec.intranet.academico.repository.HorarioRepository;
import com.cibertec.intranet.asistente.model.HistorialInteraccion;
import com.cibertec.intranet.asistente.repository.HistorialRepository;
import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.matricula.model.Matricula;
import com.cibertec.intranet.matricula.model.Pago;
import com.cibertec.intranet.matricula.repository.MatriculaRepository;
import com.cibertec.intranet.matricula.repository.PagoRepository;
import com.cibertec.intranet.profesor.model.Nota;
import com.cibertec.intranet.profesor.repository.AsistenciaRepository;
import com.cibertec.intranet.profesor.repository.NotaRepository;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final Client client;
    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialRepository historialRepository;
    private final HorarioRepository horarioRepository;
    private final MatriculaRepository matriculaRepository;
    private final NotaRepository notaRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Value("classpath:/docs/lineamientos.pdf")
    private Resource pdfResource;

    private String pdfTexto = "";

    @PostConstruct
    public void cargarPDF() throws IOException {
        try (PDDocument document = PDDocument.load(pdfResource.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            this.pdfTexto = pdfStripper.getText(document);
        }
    }

    public String procesarConsulta(String mensaje, Integer userId) {
        try {
            Usuario alumno = usuarioRepository.findById(userId).orElse(null);
            if (alumno == null) return "No se encontró información del alumno.";

            String tipoConsulta = clasificarConsulta(mensaje);

            String infoBD = switch (tipoConsulta) {
                case "academica" -> obtenerInfoAcademica(userId);
                case "deuda" -> obtenerInfoDeudas(userId);
                case "reglamento" -> "El usuario ha preguntado información del reglamento.";
                default -> "No se encontró información específica para la consulta.";
            };

            String historial = obtenerHistorialConversacion(userId);

            String prompt = """
                Eres el asistente virtual oficial de la Intranet Cibertec.

                Tienes estas FUENTES AUTORIZADAS:
                1) REGLAMENTO COMPLETO (texto extraído del PDF)
                2) Información obtenida desde la base de datos del alumno
                3) CONTEXTO DE CONVERSACIÓN (Historial)

                REGLAMENTO:
                %s

                INFORMACIÓN DE BD:
                %s

                HISTORIAL DE CONVERSACIÓN:
                %s
                
                PREGUNTA:
                %s

                Instrucciones:
                - Utiliza el HISTORIAL para mantener el contexto de la conversación.
                - Responde SOLO con información real contenida en las fuentes anteriores.
                - Si el reglamento contiene la respuesta, úsalo.
                - Si la BD contiene la respuesta, úsalo.
                - Si no está en ninguna fuente, dilo.
                - No agregar asteriscos ni simbolos, pero si emoijs.
                - Si te piden un tip acerca de un curso daselo de manera breve y concisa.

                """.formatted(pdfTexto, infoBD,historial ,mensaje);

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash",
                    Content.builder().parts(List.of(Part.fromText(prompt))).build(),
                    null
            );
            String respuestaIA = response.text();
            HistorialInteraccion h = new HistorialInteraccion();
            h.setIdUsuario(userId);
            h.setPreguntaUsuario(mensaje);
            h.setRespuestaIa(respuestaIA);
            h.setFecha(LocalDateTime.now());

            historialRepository.save(h);

            return respuestaIA;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al consultar a Gemini: " + e.getMessage();
        }
    }

    private String obtenerHistorialConversacion(Integer userId) {
        List<HistorialInteraccion> historial = historialRepository.findTop5ByIdUsuarioOrderByFechaDesc(userId);

        if (historial.isEmpty()) {
            return "No hay historial previo con este usuario.";
        }

        Collections.reverse(historial);

        return historial.stream()
                .map(h -> String.format(
                        "User: %s\nAsistente: %s",
                        h.getPreguntaUsuario(),
                        h.getRespuestaIa()
                ))
                .collect(Collectors.joining("\n---\n"));
    }

    private String clasificarConsulta(String mensaje) throws IOException {
        String prompt = """
                Eres un asistente que clasifica consultas de estudiantes. 
                Debes decidir de dónde debe salir la respuesta.
                
                Criterios:
                - "reglamento": cualquier pregunta que tenga que ver con 
                    reglas, políticas, requisitos, notas mínimas, sanciones,
                    aprobaciones, desaprobaciones, procesos, lineamientos.
                - "academica": preguntas específicas sobre cursos, profesores, notas del alumno,
                    horarios, asistencias o contenido de clases.
                - "deuda": pagos pendientes, cuotas, matrícula.
                - "desconocida": si no corresponde a ninguna.
                
                Devuelve solo uno: academica, deuda, reglamento o desconocida.
                
                Mensaje del usuario: "%s"
                """.formatted(mensaje);


        GenerateContentResponse resp = client.models.generateContent(
                "gemini-2.5-flash",
                Content.builder().parts(List.of(Part.fromText(prompt))).build(),
                null
        );

        String tipo = resp.text().trim().toLowerCase();
        if (tipo.contains("academica")) return "academica";
        if (tipo.contains("deuda")) return "deuda";
        if (tipo.contains("reglamento")) return "reglamento";
        return "desconocida";
    }

    private String obtenerInfoDeudas(Integer userId) {
        Usuario alumno = usuarioRepository.findById(userId).orElse(null);
        if (alumno == null) return "No se encontró información del alumno.";

        List<Pago> pendientes = pagoRepository.findAll().stream()
                .filter(p -> p.getMatricula().getAlumno().getIdUsuario().equals(alumno.getIdUsuario()))
                .filter(p -> p.getEstadoPago().getIdEstadoPago() == 1)
                .toList();

        if (pendientes.isEmpty()) return "El alumno no tiene deudas pendientes. Está al día.";

        double total = pendientes.stream().mapToDouble(p -> p.getMonto().doubleValue()).sum();
        return "Deudas: %d cuotas pendientes. Total S/ %.2f".formatted(pendientes.size(), total);
    }

    private String obtenerInfoAcademica(Integer userId) {
        Usuario alumno = usuarioRepository.findById(userId).orElse(null);
        if (alumno == null) return "No se encontró información del alumno.";

        Matricula m = matriculaRepository.findByAlumno_IdUsuario(alumno.getIdUsuario());
        if (m == null) return "El alumno no está matriculado.";

        StringBuilder sb = new StringBuilder();
        sb.append("Alumno: ").append(alumno.getNombres()).append(" ").append(alumno.getApellidos()).append(". ");
        if (m.getCiclo() != null) sb.append("Ciclo actual: ").append(m.getCiclo().getNombreCiclo()).append(". ");
        if (m.getCarrera() != null) sb.append("Carrera: ").append(m.getCarrera().getNombreCarrera()).append(". ");
        sb.append("Periodo: ").append(m.getPeriodo()).append(". ");

        for (DetalleMatricula d : m.getDetalles()) {
            Curso c = d.getCurso();
            sb.append("Curso: ").append(c.getNombreCurso()).append(". ");

            if (c.getProfesor() != null) {
                sb.append("Profesor: ").append(c.getProfesor().getNombres())
                        .append(" ").append(c.getProfesor().getApellidos()).append(". ");
            }

            List<Horario> horarios = horarioRepository.findByCursoIdCurso(c.getIdCurso());
            if (!horarios.isEmpty()) {
                sb.append("Horario: ");
                horarios.forEach(h -> sb.append(h.getDiaSemana())
                        .append(" ").append(h.getHoraInicio().toString().substring(0, 5)).append("; "));
            }

            int asistencias = asistenciaRepository.countByAlumno_IdUsuarioAndSesion_Curso_IdCurso(userId, c.getIdCurso());
            int faltas = asistenciaRepository.countByAlumno_IdUsuarioAndSesion_Curso_IdCursoAndIdEstado(userId, c.getIdCurso(), 2);
            sb.append(" Asistencias: ").append(asistencias).append(". Faltas: ").append(faltas).append(". ");

            Nota nota = notaRepository.findByDetalleMatricula_IdDetalle(d.getIdDetalle());
            if (nota != null) {
                sb.append("Notas: [")
                        .append("Nota1: ").append(nota.getNota1() != null ? nota.getNota1() : "no registrada aún").append(", ")
                        .append("Nota2: ").append(nota.getNota2() != null ? nota.getNota2() : "no registrada aún").append(", ")
                        .append("Nota3: ").append(nota.getNota3() != null ? nota.getNota3() : "no registrada aún").append(", ")
                        .append("Examen Final: ").append(nota.getExamenFinal() != null ? nota.getExamenFinal() : "no registrada aún").append("], ")
                        .append("Promedio Final: ").append(nota.getPromedioFinal() != null ? nota.getPromedioFinal() : "no calculado aún").append(". ");
            }
        }

        return sb.toString();
    }
}