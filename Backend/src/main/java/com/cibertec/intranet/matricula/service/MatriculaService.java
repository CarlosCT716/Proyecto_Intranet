package com.cibertec.intranet.matricula.service;

import com.cibertec.intranet.academico.model.Aula;
import com.cibertec.intranet.academico.model.Carrera;
import com.cibertec.intranet.academico.model.Ciclo;
import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.academico.model.Horario;
import com.cibertec.intranet.academico.repository.AulaRepository;
import com.cibertec.intranet.academico.repository.CarreraRepository;
import com.cibertec.intranet.academico.repository.CicloRepository;
import com.cibertec.intranet.academico.repository.CursoRepository;
import com.cibertec.intranet.academico.repository.HorarioRepository;
import com.cibertec.intranet.auditoria.annotation.Auditable;
import com.cibertec.intranet.matricula.dto.MatriculaDTO;
import com.cibertec.intranet.matricula.dto.ValidacionMatriculaDTO;
import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.matricula.model.EstadoPago;
import com.cibertec.intranet.matricula.model.Matricula;
import com.cibertec.intranet.matricula.model.Pago;
import com.cibertec.intranet.matricula.repository.DetalleMatriculaRepository;
import com.cibertec.intranet.matricula.repository.EstadoPagoRepository;
import com.cibertec.intranet.matricula.repository.MatriculaRepository;
import com.cibertec.intranet.matricula.repository.PagoRepository;
import com.cibertec.intranet.profesor.model.Nota;
import com.cibertec.intranet.profesor.repository.NotaRepository;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import com.cibertec.intranet.profesor.repository.AsistenciaRepository;
import com.cibertec.intranet.profesor.repository.SesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final PagoRepository pagoRepository;

    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final CicloRepository cicloRepository;
    private final CursoRepository cursoRepository;
    private final HorarioRepository horarioRepository;
    private final AulaRepository aulaRepository;
    
    private final NotaRepository notaRepository;
    private final EstadoPagoRepository estadoPagoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final SesionRepository sesionRepository;

    @Auditable(accion = "CREACION", tabla = "tb_matricula")
    @Transactional
    public Matricula registrarMatricula(MatriculaDTO dto) {

        Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + dto.getIdAlumno()));

        Carrera carrera = carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + dto.getIdCarrera()));

        Ciclo ciclo = cicloRepository.findById(dto.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado con ID: " + dto.getIdCiclo()));

        if (dto.getIdCursos() == null || dto.getIdCursos().isEmpty()) {
            throw new RuntimeException("Debe seleccionar al menos un curso para matricularse.");
        }

        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setCarrera(carrera);
        matricula.setCiclo(ciclo);
        matricula.setPeriodo(dto.getPeriodo());
        matricula.setFechaMatricula(LocalDateTime.now());

        Matricula matriculaGuardada = matriculaRepository.save(matricula);

        int totalCreditos = 0;

        for (Integer idCurso : dto.getIdCursos()) {
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + idCurso));
   
            if (curso.getCupoActual() <= 0) {
                throw new RuntimeException("El curso " + curso.getNombreCurso() + " no tiene vacantes disponibles.");
            }
            curso.setCupoActual(curso.getCupoActual() - 1);
            cursoRepository.save(curso);
            List<Horario> horarios = horarioRepository.findByCursoIdCurso(idCurso);
            if (!horarios.isEmpty()) {

                Aula aula = horarios.get(0).getAula();
                if (aula.getAforoActual() <= 0) {
                    throw new RuntimeException("El aula " + aula.getDescripcion() + " no tiene aforo disponible para el curso " + curso.getNombreCurso());
                }
                aula.setAforoActual(aula.getAforoActual() - 1);
                aulaRepository.save(aula);
            }

            totalCreditos += curso.getCreditos();

            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setMatricula(matriculaGuardada);
            detalle.setCurso(curso);
            DetalleMatricula detalleGuardado = detalleMatriculaRepository.save(detalle);

            Nota notaInicial = new Nota();
            notaInicial.setDetalleMatricula(detalleGuardado);
            notaRepository.save(notaInicial);
        }

        generarPagosAutomaticos(matriculaGuardada, totalCreditos);

        return matriculaGuardada;
    }

    private void generarPagosAutomaticos(Matricula matricula, int totalCreditos) {
        LocalDate fechaBase = LocalDate.now();

        crearCuota(matricula, "Derecho de Matrícula", new BigDecimal("300.00"), fechaBase, 2);

        BigDecimal costoPorCredito = new BigDecimal("75.00");
        BigDecimal totalBoleta = costoPorCredito.multiply(new BigDecimal(totalCreditos));

        int numeroCuotas = 6;
        BigDecimal montoCuota = totalBoleta.divide(new BigDecimal(numeroCuotas), 2, RoundingMode.HALF_UP);

        for (int i = 1; i <= numeroCuotas; i++) {
            String concepto = "Mensualidad " + i + " (Costo Créditos)";
            LocalDate vencimiento = fechaBase.plusMonths(i - 1).withDayOfMonth(28);
            Integer estado = 1;

            if (i == numeroCuotas) {
                BigDecimal pagadoHastaAhora = montoCuota.multiply(new BigDecimal(numeroCuotas - 1));
                BigDecimal ultimaCuota = totalBoleta.subtract(pagadoHastaAhora);
                crearCuota(matricula, concepto, ultimaCuota, vencimiento, estado);
            } else {
                crearCuota(matricula, concepto, montoCuota, vencimiento, estado);
            }
        }
    }

    private void crearCuota(Matricula m, String concepto, BigDecimal monto, LocalDate vencimiento, Integer idEstado) {
        Pago pago = new Pago();
        pago.setMatricula(m);
        pago.setConcepto(concepto);
        pago.setMonto(monto);
        pago.setFechaVencimiento(vencimiento);
        EstadoPago estado = estadoPagoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado de pago no encontrado: " + idEstado));
        pago.setEstadoPago(estado);
        if (idEstado == 2) {
            pago.setFechaPago(LocalDate.now());
        } else {
            pago.setFechaPago(null);
        }

        pagoRepository.save(pago);
    }
    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    public ValidacionMatriculaDTO validarRequisitosMatricula(Integer idAlumno) {
        ValidacionMatriculaDTO respuesta = new ValidacionMatriculaDTO();
        List<String> rechazos = new ArrayList<>();
        
        // 1. Obtener última matrícula
        Matricula ultimaMatricula = matriculaRepository.findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(idAlumno)
                .orElse(null);

        if (ultimaMatricula == null) {
            // Es alumno nuevo o nunca se matriculó -> Apto para ciclo 1
            respuesta.setAptoParaMatricula(true);
            respuesta.setSiguienteCicloSugericdo(1);
            respuesta.setMensaje("Alumno nuevo o sin historial. Apto para matrícula inicial.");
            return respuesta;
        }

        // 2. Validar Pagos (Deuda 0)
        List<Pago> pagosPendientes = pagoRepository.buscarPendientesVisibles(
                ultimaMatricula.getIdMatricula(), 
                1, 
                LocalDate.now().plusYears(1) // Buscar cualquier pendiente futuro también
        );
        
        if (!pagosPendientes.isEmpty()) {
            rechazos.add("Tiene " + pagosPendientes.size() + " cuotas pendientes de pago.");
        }

        for (DetalleMatricula detalle : ultimaMatricula.getDetalles()) {
            // Notas
            Nota nota = notaRepository.findByDetalleMatricula_IdDetalle(detalle.getIdDetalle());
            if (nota == null || nota.getPromedioFinal() == null || nota.getPromedioFinal().doubleValue() < 13.0) {
                rechazos.add("Curso desaprobado: " + detalle.getCurso().getNombreCurso());
            }

            long totalSesiones = sesionRepository.countByCurso_IdCurso(detalle.getCurso().getIdCurso());
            long faltas = asistenciaRepository.countByAlumno_IdUsuarioAndSesion_Curso_IdCursoAndIdEstado(idAlumno, detalle.getCurso().getIdCurso(), 2); // 2 = Falta
            
            if (totalSesiones > 0) {
                double porcentajeAsistencia = 100.0 - ((double) faltas / totalSesiones * 100.0);
                if (porcentajeAsistencia < 70.0) {
                    rechazos.add("Inhabilitado por inasistencia en: " + detalle.getCurso().getNombreCurso() + " (" + (int)porcentajeAsistencia + "%)");
                }
            }
        }

        if (rechazos.isEmpty()) {
            respuesta.setAptoParaMatricula(true);
            respuesta.setMensaje("Cumple con todos los requisitos.");
            // Lógica simple para sugerir siguiente ciclo (asumiendo IDs secuenciales)
            respuesta.setSiguienteCicloSugericdo(ultimaMatricula.getCiclo().getIdCiclo() + 1);
        } else {
            respuesta.setAptoParaMatricula(false);
            respuesta.setMensaje("No cumple los requisitos para la siguiente matrícula.");
            respuesta.setMotivosRechazo(rechazos);
        }

        return respuesta;
    }
}