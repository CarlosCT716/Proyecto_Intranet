package com.cibertec.intranet.profesor.service;

import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.academico.model.Horario;
import com.cibertec.intranet.academico.repository.CursoRepository;
import com.cibertec.intranet.academico.repository.HorarioRepository;
import com.cibertec.intranet.auditoria.annotation.Auditable;
import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.matricula.repository.DetalleMatriculaRepository;
import com.cibertec.intranet.profesor.dto.*;
import com.cibertec.intranet.profesor.model.Asistencia;
import com.cibertec.intranet.profesor.model.Nota;
import com.cibertec.intranet.profesor.model.Sesion;
import com.cibertec.intranet.profesor.repository.AsistenciaRepository;
import com.cibertec.intranet.profesor.repository.NotaRepository;
import com.cibertec.intranet.profesor.repository.SesionRepository;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final NotaRepository notaRepository;
    private final SesionRepository sesionRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HorarioRepository horarioRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;

    public TeacherDashboardDTO obtenerDashboardProfesor(Integer idProfesor) {
        TeacherDashboardDTO dashboard = new TeacherDashboardDTO();

        List<Curso> cursosEntity = cursoRepository.findByProfesorIdUsuarioAndActivoTrue(idProfesor);

        List<TeacherDashboardDTO.CursoAsignadoDTO> cursosDTO = cursosEntity.stream().map(c -> {
            TeacherDashboardDTO.CursoAsignadoDTO dto = new TeacherDashboardDTO.CursoAsignadoDTO();
            dto.setIdCurso(c.getIdCurso());
            dto.setNombre(c.getNombreCurso());
            dto.setSeccion("T" + c.getCiclo().getIdCiclo() + "AN");

            Horario h = horarioRepository.findFirstByCursoIdCurso(c.getIdCurso());
            dto.setAula(h != null ? h.getAula().getDescripcion() : "Virtual");

            if (c.getNombreCurso().toLowerCase().contains("datos")) {
                dto.setIcon("fa-database");
                dto.setColorBg("bg-purple-100");
                dto.setColorTxt("text-purple-700");
            } else {
                dto.setIcon("fa-code");
                dto.setColorBg("bg-blue-100");
                dto.setColorTxt("text-[#0B4D6C]");
            }
            return dto;
        }).collect(Collectors.toList());

        dashboard.setCursos(cursosDTO);

        // 2. AGENDA DE HOY
        String diaHoy = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        if(diaHoy.equals("MIÉRCOLES")) diaHoy = "MIERCOLES";
        if(diaHoy.equals("SÁBADO")) diaHoy = "SABADO";

        List<Horario> horariosHoy = horarioRepository.findByCursoProfesorIdUsuarioAndDiaSemana(idProfesor, diaHoy);

        List<TeacherDashboardDTO.ClaseHoyDTO> agenda = horariosHoy.stream().map(h -> {
            TeacherDashboardDTO.ClaseHoyDTO dto = new TeacherDashboardDTO.ClaseHoyDTO();
            dto.setCurso(h.getCurso().getNombreCurso());
            dto.setHorario(h.getHoraInicio() + " - " + h.getHoraFin());
            return dto;
        }).collect(Collectors.toList());

        dashboard.setAgendaHoy(agenda);

        List<Sesion> sesionesPendientes = sesionRepository.findByCursoProfesorIdUsuarioAndEstadoSesion(idProfesor, "PROGRAMADA");

        List<TeacherDashboardDTO.SesionPendienteDTO> asistenciasDTO = sesionesPendientes.stream()
                .sorted(Comparator.comparing(Sesion::getFecha)) 
                .limit(2) 
                .map(s -> {
                    TeacherDashboardDTO.SesionPendienteDTO dto = new TeacherDashboardDTO.SesionPendienteDTO();
                    dto.setIdSesion(s.getIdSesion());
                    dto.setCurso(s.getCurso().getNombreCurso());
                    dto.setFecha(s.getFecha().toString());

                    // Lógica visual
                    if (s.getFecha().isEqual(LocalDate.now())) {
                        dto.setEstado("Hoy");
                    } else if (s.getFecha().isBefore(LocalDate.now())) {
                        dto.setEstado("Vencida");
                    } else {
                        dto.setEstado("Próxima");
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        dashboard.setAsistenciasPendientes(asistenciasDTO);


        // 4. CARGA DE NOTAS (LIMITADO A 2)
        List<TeacherDashboardDTO.AvanceNotasDTO> avances = new ArrayList<>();

        for (Curso c : cursosEntity) {
            List<Nota> notasCurso = notaRepository.findByDetalleMatricula_Curso_IdCurso(c.getIdCurso());

            if (notasCurso.isEmpty()) continue;

            int total = notasCurso.size();
            long t1 = notasCurso.stream().filter(n -> n.getNota1() != null).count();
            long t2 = notasCurso.stream().filter(n -> n.getNota2() != null).count();
            long t3 = notasCurso.stream().filter(n -> n.getNota3() != null).count();
            long ef = notasCurso.stream().filter(n -> n.getExamenFinal() != null).count();

            TeacherDashboardDTO.AvanceNotasDTO avance = new TeacherDashboardDTO.AvanceNotasDTO();
            avance.setCurso(c.getNombreCurso());

            // Prioridad: mostrar la primera nota que falte llenar
            if (t1 < total) {
                avance.setEvaluacion("Nota 1 (T1)");
                avance.setPorcentaje((int) ((t1 * 100.0) / total));
                avance.setEstado("Pendiente");
            } else if (t2 < total) {
                avance.setEvaluacion("Nota 2 (T2)");
                avance.setPorcentaje((int) ((t2 * 100.0) / total));
                avance.setEstado("Pendiente");
            } else if (t3 < total) {
                avance.setEvaluacion("Nota 3 (T3)");
                avance.setPorcentaje((int) ((t3 * 100.0) / total));
                avance.setEstado("Pendiente");
            } else {
                avance.setEvaluacion("Examen Final");
                avance.setPorcentaje((int) ((ef * 100.0) / total));
                avance.setEstado(ef == total ? "Completado" : "Pendiente");
            }

            avances.add(avance);
        }

        dashboard.setCargaNotas(avances.stream().limit(2).collect(Collectors.toList()));

        return dashboard;
    }

    public List<AgendaProfesorDTO> obtenerAgendaCompleta(Integer idProfesor) {
        List<Curso> cursos = cursoRepository.findByProfesor_IdUsuario(idProfesor);
        List<Integer> idsCursos = cursos.stream().map(Curso::getIdCurso).collect(Collectors.toList());

        List<Horario> horarios = horarioRepository.findByCursoIdCursoIn(idsCursos);

        return horarios.stream().map(h -> {
            AgendaProfesorDTO dto = new AgendaProfesorDTO();
            dto.setIdCurso(h.getCurso().getIdCurso());
            dto.setNombreCurso(h.getCurso().getNombreCurso());
            dto.setDia(h.getDiaSemana().toUpperCase());
            dto.setHoraInicio(h.getHoraInicio().toString());
            dto.setHoraFin(h.getHoraFin().toString());
            dto.setAula(h.getAula().getDescripcion());

            boolean esVirtual = h.getAula().getDescripcion().toLowerCase().contains("virtual");
            dto.setModalidad(esVirtual ? "Virtual" : "Presencial");

            if (h.getCurso().getNombreCurso().length() % 2 == 0) {
                dto.setColor("bg-blue-100 border-l-4 border-blue-600 text-blue-800");
            } else {
                dto.setColor("bg-purple-100 border-l-4 border-purple-600 text-purple-800");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public List<Curso> listarCursosAsignados(Integer idProfesor, Integer idCiclo, Integer idCarrera) {
        List<Curso> cursos = cursoRepository.findByProfesor_IdUsuario(idProfesor);

        return cursos.stream()
                .filter(c -> idCiclo == null || c.getCiclo().getIdCiclo().equals(idCiclo))
                .filter(c -> idCarrera == null || c.getCarrera().getIdCarrera().equals(idCarrera))
                .collect(Collectors.toList());
    }

    public List<NotaDTO> listarNotasPorCurso(Integer idCurso) {
        return notaRepository.findByDetalleMatricula_Curso_IdCurso(idCurso).stream()
                .map(this::convertirNotaADTO)
                .collect(Collectors.toList());
    }

    @Auditable(accion = "ACTUALIZACION_MASIVA", tabla = "tb_notas")
    @Transactional
    public List<NotaDTO> actualizarNotasMasivo(List<NotaDTO> dtos) {
        List<NotaDTO> actualizados = new ArrayList<>();
        for (NotaDTO dto : dtos) {
            Nota nota = notaRepository.findById(dto.getIdNota())
                    .orElseThrow(() -> new RuntimeException("Nota no encontrada: " + dto.getIdNota()));

            nota.setNota1(dto.getNota1());
            nota.setNota2(dto.getNota2());
            nota.setNota3(dto.getNota3());
            nota.setExamenFinal(dto.getExamenFinal());

            actualizados.add(convertirNotaADTO(notaRepository.save(nota)));
        }
        return actualizados;
    }

    public List<SesionDTO> listarSesionesPorCurso(Integer idCurso) {
        return sesionRepository.findByCurso_IdCursoOrderByFechaDesc(idCurso).stream()
                .map(s -> {
                    SesionDTO dto = new SesionDTO();
                    dto.setIdSesion(s.getIdSesion());
                    dto.setFecha(s.getFecha());
                    dto.setTemaTratado(s.getTemaTratado());
                    dto.setEstado(s.getEstadoSesion());
                    return dto;
                }).collect(Collectors.toList());
    }

    public List<AsistenciaDetalleDTO> obtenerDetalleAsistencia(Integer idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Integer idCurso = sesion.getCurso().getIdCurso();

        List<DetalleMatricula> matriculados = detalleMatriculaRepository.findByCurso_IdCurso(idCurso);

        return matriculados.stream().map(m -> {
            AsistenciaDetalleDTO dto = new AsistenciaDetalleDTO();
            Usuario alumno = m.getMatricula().getAlumno();
            dto.setIdAlumno(alumno.getIdUsuario());
            dto.setNombreAlumno(alumno.getApellidos() + ", " + alumno.getNombres());

            Optional<Asistencia> asistenciaExistente = asistenciaRepository
                    .findBySesion_IdSesionAndAlumno_IdUsuario(idSesion, alumno.getIdUsuario());

            if (asistenciaExistente.isPresent()) {
                dto.setIdEstado(asistenciaExistente.get().getIdEstado());
                dto.setObservacion(asistenciaExistente.get().getObservacion());
            } else {
                dto.setIdEstado(0);
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Auditable(accion = "REGISTRO_ASISTENCIA", tabla = "tb_asistencia")
    @Transactional
    public void registrarAsistenciaMasiva(Integer idSesion, List<AsistenciaDTO> listaAsistencia) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        for (AsistenciaDTO dto : listaAsistencia) {
            Optional<Asistencia> existente = asistenciaRepository
                    .findBySesion_IdSesionAndAlumno_IdUsuario(idSesion, dto.getIdAlumno());

            Asistencia asistencia = existente.orElse(new Asistencia());

            if (existente.isEmpty()) {
                asistencia.setSesion(sesion);
                Usuario alumnoRef = usuarioRepository.getReferenceById(dto.getIdAlumno());
                asistencia.setAlumno(alumnoRef);
            }

            asistencia.setIdEstado(dto.getEstado());
            asistencia.setObservacion(dto.getObservacion());

            asistenciaRepository.save(asistencia);
        }
    }

    @Auditable(accion = "FINALIZAR_SESION", tabla = "tb_sesion")
    @Transactional
    public void finalizarSesion(Integer idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setEstadoSesion("FINALIZADA");
        sesionRepository.save(sesion);
    }

    private NotaDTO convertirNotaADTO(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setIdNota(n.getIdNota());
        Usuario alumno = n.getDetalleMatricula().getMatricula().getAlumno();

        dto.setIdAlumno(alumno.getIdUsuario());
        dto.setNombreAlumno(alumno.getApellidos() + ", " + alumno.getNombres());
        dto.setNota1(n.getNota1());
        dto.setNota2(n.getNota2());
        dto.setNota3(n.getNota3());
        dto.setExamenFinal(n.getExamenFinal());
        dto.setPromedioFinal(n.getPromedioFinal());
        return dto;
    }
}