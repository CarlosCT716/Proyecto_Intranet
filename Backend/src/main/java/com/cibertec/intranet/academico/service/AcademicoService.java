package com.cibertec.intranet.academico.service;

import com.cibertec.intranet.academico.dto.*;
import com.cibertec.intranet.academico.model.*;
import com.cibertec.intranet.academico.repository.*;
import com.cibertec.intranet.auditoria.annotation.Auditable;
import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.matricula.model.Matricula;
import com.cibertec.intranet.matricula.model.Pago;
import com.cibertec.intranet.matricula.repository.DetalleMatriculaRepository;
import com.cibertec.intranet.matricula.repository.MatriculaRepository;
import com.cibertec.intranet.matricula.repository.PagoRepository;
import com.cibertec.intranet.profesor.model.Asistencia;
import com.cibertec.intranet.profesor.model.Nota;
import com.cibertec.intranet.profesor.repository.AsistenciaRepository;
import com.cibertec.intranet.profesor.repository.NotaRepository;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import com.cibertec.intranet.profesor.model.Sesion; 
import com.cibertec.intranet.profesor.repository.SesionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicoService {

    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;
    private final CicloRepository cicloRepository;
    private final UsuarioRepository usuarioRepository;
    private final AulaRepository aulaRepository;
    private final HorarioRepository horarioRepository;
    private final MatriculaRepository matriculaRepo;
    private final PagoRepository pagoRepo;
    private final NotaRepository notaRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepo;
    private final AsistenciaRepository asistenciaRepo;
    private final SesionRepository sesionRepository;

    private static final Map<String, Integer> DIAS_SEMANA = Map.of(
            "LUNES", 1,
            "MARTES", 2,
            "MIERCOLES", 3,
            "MIÉRCOLES", 3,
            "JUEVES", 4,
            "VIERNES", 5,
            "SABADO", 6,
            "SÁBADO", 6,
            "DOMINGO", 7
    );

    public DashboardDTO obtenerDashboardAlumno(Integer idAlumno) {
        DashboardDTO dashboard = new DashboardDTO();

        Matricula matricula = matriculaRepo.findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(idAlumno)
                .orElseThrow(() -> new RuntimeException("El alumno no tiene matrícula activa."));

        List<DashboardDTO.CursoResumenDTO> cursosResumen = matricula.getDetalles().stream()
                .limit(2)
                .map(detalle -> {
                    DashboardDTO.CursoResumenDTO dto = new DashboardDTO.CursoResumenDTO();
                    dto.setNombre(detalle.getCurso().getNombreCurso());
                    dto.setSiglas(generarSiglas(detalle.getCurso().getNombreCurso()));
                    dto.setModalidad("Presencial"); 

                    try {
                        BigDecimal prom = notaRepository.obtenerPromedioPorDetalle(detalle.getIdDetalle());
                        dto.setPromedio(prom != null ? prom.intValue() : 0);
                    } catch (Exception e) {
                        dto.setPromedio(0);
                    }
                    return dto;
                }).collect(Collectors.toList());

        dashboard.setCursos(cursosResumen);

        List<Integer> idsCursos = matricula.getDetalles().stream()
                .map(d -> d.getCurso().getIdCurso())
                .collect(Collectors.toList());

        List<Horario> horarios = horarioRepository.findByCursoIdCursoIn(idsCursos);

        List<DashboardDTO.ClaseDTO> proximasClases = horarios.stream()
                .filter(h -> h.getDiaSemana() != null)
                .sorted(this::compararCercaniaDia)
                .limit(2)
                .map(h -> {
                    DashboardDTO.ClaseDTO dto = new DashboardDTO.ClaseDTO();
                    dto.setCurso(h.getCurso().getNombreCurso());
                    dto.setAula(h.getAula().getDescripcion());
                    String diaCapitalizado = capitalize(h.getDiaSemana());
                    dto.setHorario(diaCapitalizado + " " + h.getHoraInicio() + " - " + h.getHoraFin());
                    dto.setEsVirtual(h.getAula().getDescripcion().toLowerCase().contains("virtual"));
                    return dto;
                }).collect(Collectors.toList());

        dashboard.setProximasClases(proximasClases);
        LocalDate finDeMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        List<Pago> pagosPendientes = pagoRepo.buscarPendientesVisibles(
                matricula.getIdMatricula(),
                1,
                finDeMes
        );

        DashboardDTO.EstadoCuentaDTO estadoCuenta = new DashboardDTO.EstadoCuentaDTO();

        if (!pagosPendientes.isEmpty()) {
            Pago pago = pagosPendientes.get(0);
            estadoCuenta.setTieneDeuda(true);
            estadoCuenta.setConcepto(pago.getConcepto());
            estadoCuenta.setMonto(pago.getMonto().doubleValue());
            estadoCuenta.setVencimiento(pago.getFechaVencimiento().toString());
        } else {
            estadoCuenta.setTieneDeuda(false);
            estadoCuenta.setConcepto("Al día");
            estadoCuenta.setMonto(0.0);
            estadoCuenta.setVencimiento("-");
        }

        dashboard.setEstadoCuenta(estadoCuenta);

        return dashboard;
    }

    public List<CursoMatriculadoDTO> listarCursosMatriculados(Integer idAlumno) {
        Matricula matricula = matriculaRepo.findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(idAlumno)
                .orElseThrow(() -> new RuntimeException("Sin matrícula activa"));

        return matricula.getDetalles().stream().map(detalle -> {
            Curso curso = detalle.getCurso();
            CursoMatriculadoDTO dto = new CursoMatriculadoDTO();
            dto.setIdCurso(curso.getIdCurso());
            dto.setNombreCurso(curso.getNombreCurso());
            dto.setSeccion("T" + curso.getCiclo().getIdCiclo() + "WN");

            if (curso.getProfesor() != null) {
                dto.setNombreProfesor(curso.getProfesor().getNombres() + " " + curso.getProfesor().getApellidos());
            }

            List<Horario> horarios = horarioRepository.findByCursoIdCurso(curso.getIdCurso());
            if (!horarios.isEmpty()) {
                Aula aula = horarios.get(0).getAula();
                dto.setAula(aula.getDescripcion());
                dto.setModalidad(aula.getDescripcion().toLowerCase().contains("virtual") ? "Virtual" : "Presencial");
            } else {
                dto.setAula("Por definir");
                dto.setModalidad("Presencial");
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public CursoDetalleDTO obtenerDetalleCurso(Integer idAlumno, Integer idCurso) {
        CursoDetalleDTO dto = new CursoDetalleDTO();

        DetalleMatricula detalle = detalleMatriculaRepo.findByAlumnoAndCurso(idAlumno, idCurso)
                .orElseThrow(() -> new RuntimeException("No estás matriculado en este curso"));

        dto.setNombreCurso(detalle.getCurso().getNombreCurso());

        Nota nota = notaRepository.findByDetalleMatricula_IdDetalle(detalle.getIdDetalle());

        if (nota == null) {
            nota = new Nota();
        }

        dto.setNota1(nota.getNota1());
        dto.setNota2(nota.getNota2());
        dto.setNota3(nota.getNota3());
        dto.setExamenFinal(nota.getExamenFinal());
        dto.setPromedio(nota.getPromedioFinal());

        List<Asistencia> asistencias = asistenciaRepo.listarPorAlumnoYCurso(idAlumno, idCurso);

        List<CursoDetalleDTO.AsistenciaDTO> historial = new ArrayList<>();
        int asistenciasCount = 0;
        int totalClases = asistencias.size();

        for (int i = 0; i < totalClases; i++) {
            Asistencia a = asistencias.get(i);
            CursoDetalleDTO.AsistenciaDTO asisDto = new CursoDetalleDTO.AsistenciaDTO();
            asisDto.setSesion("Sesión " + String.format("%02d", (i + 1)));
            asisDto.setFecha(a.getSesion().getFecha());
            asisDto.setTema(a.getSesion().getTemaTratado());

            String descEstado;
            if (a.getIdEstado() == 1) descEstado = "Presente";
            else if (a.getIdEstado() == 2) descEstado = "Falta";
            else if (a.getIdEstado() == 3) descEstado = "Tardanza";
            else if (a.getIdEstado() == 4) descEstado = "Justificado";
            else descEstado = "Desconocido";

            asisDto.setEstado(descEstado);

            if (a.getIdEstado() == 1 || a.getIdEstado() == 3 || a.getIdEstado() == 4) asistenciasCount++;

            historial.add(asisDto);
        }

        dto.setHistorialAsistencia(historial);

        if (totalClases > 0) {
            int porcentaje = (asistenciasCount * 100) / totalClases;
            dto.setPorcentajeAsistencia(porcentaje + "%");
        } else {
            dto.setPorcentajeAsistencia("100%");
        }

        return dto;
    }

    public List<HorarioAlumnoDTO> obtenerHorarioAlumno(Integer idAlumno) {
        Matricula matricula = matriculaRepo.findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno sin matrícula activa"));

        List<Integer> idsCursos = matricula.getDetalles().stream()
                .map(d -> d.getCurso().getIdCurso())
                .collect(Collectors.toList());

        List<Horario> horarios = horarioRepository.findByCursoIdCursoIn(idsCursos);

        return horarios.stream().map(h -> {
            HorarioAlumnoDTO dto = new HorarioAlumnoDTO();
            dto.setIdCurso(h.getCurso().getIdCurso());
            dto.setNombreCurso(h.getCurso().getNombreCurso());
            dto.setSeccion("T" + h.getCurso().getCiclo().getIdCiclo() + "WN");
            dto.setDia(h.getDiaSemana().toUpperCase()); 
            dto.setHoraInicio(h.getHoraInicio().toString());
            dto.setHoraFin(h.getHoraFin().toString());
            dto.setAula(h.getAula().getDescripcion());

            if(h.getCurso().getProfesor() != null) {
                dto.setProfesor(h.getCurso().getProfesor().getApellidos());
            }

            if (h.getAula().getDescripcion().toLowerCase().contains("virtual")) {
                dto.setColor("bg-purple-600");
            } else {
                dto.setColor("bg-[#0B4D6C]");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public List<Carrera> listarCarreras() { return carreraRepository.findAll(); }


    public Carrera obtenerCarrera(Integer id) {
        return carreraRepository.findById(id).orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
    }

    @Auditable(accion = "CREACION", tabla = "tb_carrera")
    @Transactional
    public Carrera guardarCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public List<Ciclo> listarCiclos() { return cicloRepository.findAll(); }



    public Ciclo obtenerCiclo(Integer id) {
        return cicloRepository.findById(id).orElseThrow(() -> new RuntimeException("Ciclo no encontrado"));
    }

    @Auditable(accion = "CREACION", tabla = "tb_ciclo")
    @Transactional
    public Ciclo guardarCiclo(Ciclo ciclo) {
        return cicloRepository.save(ciclo);
    }

    public List<Aula> listarAulas() { return aulaRepository.findAll(); }

    public List<Aula> listarAulasActivas() { return aulaRepository.findByActivoTrue(); }

    public Aula obtenerAula(Integer id) {
        return aulaRepository.findById(id).orElseThrow(() -> new RuntimeException("Aula no encontrada"));
    }

    @Auditable(accion = "CREACION", tabla = "tb_aula")
    @Transactional
    public Aula guardarAula(Aula aula) {
        if(aula.getIdAula() == null) aula.setActivo(true);
        return aulaRepository.save(aula);
    }

    @Auditable(accion = "ESTADO", tabla = "tb_aula")
    @Transactional
    public void cambiarEstadoAula(Integer id) {
        Aula aula = obtenerAula(id);
        aula.setActivo(!aula.getActivo());
        aulaRepository.save(aula);
    }

    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertirCursoADTO)
                .collect(Collectors.toList());
    }

    public List<CursoDTO> listarCursosActivos() {
        return cursoRepository.findByActivoTrue().stream()
                .map(this::convertirCursoADTO)
                .collect(Collectors.toList());
    }

    public CursoDTO obtenerCurso(Integer id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        return convertirCursoADTO(curso);
    }

    @Auditable(accion = "CREACION", tabla = "tb_curso")
    @Transactional
    public CursoDTO guardarCurso(CursoCreateDTO dto) {
        Curso curso = new Curso();
        curso.setActivo(true);
        return registrarDatosCurso(curso, dto);
    }

    @Auditable(accion = "ACTUALIZACIÓN", tabla = "tb_curso")
    @Transactional
    public CursoDTO actualizarCurso(Integer id, CursoCreateDTO dto) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        return registrarDatosCurso(curso, dto);
    }

    @Auditable(accion = "ESTADO", tabla = "tb_curso")
    @Transactional
    public void cambiarEstadoCurso(Integer id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        curso.setActivo(!curso.getActivo());
        cursoRepository.save(curso);
    }

    public List<CursoDTO> filtrarCursos(Integer idCarrera, Integer idCiclo) {
        List<Curso> cursos = cursoRepository.findByCarreraIdCarreraAndCicloIdCiclo(idCarrera, idCiclo);
        return cursos.stream().map(this::convertirCursoADTO).toList();
    }

    public List<HorarioDTO> listarHorarios() {
        return horarioRepository.findAll().stream()
                .map(this::convertirHorarioADTO)
                .collect(Collectors.toList());
    }

    public List<HorarioDTO> listarHorariosActivos() {
        return horarioRepository.findByActivoTrue().stream()
                .map(this::convertirHorarioADTO)
                .collect(Collectors.toList());
    }

    public HorarioDTO obtenerHorario(Integer id) {
        Horario horario = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        return convertirHorarioADTO(horario);
    }

    @Auditable(accion = "CREACION", tabla = "tb_horario")
    @Transactional
    public HorarioDTO crearHorario(HorarioCreateDTO dto) {
        Horario horario = new Horario();
        horario.setActivo(true);
   
        if (dto.getIdAula() == null) {
            throw new RuntimeException("Debe seleccionar un aula.");
        }
        if (dto.getIdCurso() == null) {
            throw new RuntimeException("Debe seleccionar un curso.");
        }

        Aula aula = aulaRepository.findById(dto.getIdAula())
                .orElseThrow(() -> new RuntimeException("Aula no encontrada con ID: " + dto.getIdAula()));
        horario.setAula(aula);

        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + dto.getIdCurso()));
        horario.setCurso(curso);

        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());

        Horario horarioGuardado = horarioRepository.save(horario);

        if (dto.getFechaInicio() != null) {
            generarSesionesAutomaticas(horarioGuardado, dto.getFechaInicio());
        }
        
        return convertirHorarioADTO(horarioGuardado);
    }

    private void generarSesionesAutomaticas(Horario horario, LocalDate fechaInicio) {
        DayOfWeek diaObjetivo = mapearDiaSemana(horario.getDiaSemana());
        LocalDate fechaCalculada = fechaInicio.with(TemporalAdjusters.nextOrSame(diaObjetivo));

        List<Sesion> sesionesNuevas = new ArrayList<>();

        long sesionesExistentes = sesionRepository.countByCurso_IdCurso(horario.getCurso().getIdCurso());

        for (int i = 1; i <= 14; i++) {
            boolean existe = sesionRepository.existsByCursoIdCursoAndFecha(horario.getCurso().getIdCurso(), fechaCalculada);
            
            if (!existe) {
                Sesion sesion = new Sesion();
                sesion.setCurso(horario.getCurso());
                sesion.setFecha(fechaCalculada);
                sesion.setEstadoSesion("PROGRAMADA");
                
                long numeroSesion = sesionesExistentes + i;
                sesion.setTemaTratado("Sesión " + numeroSesion + ": Tema por definir");
                
                sesionesNuevas.add(sesion);
            }

            fechaCalculada = fechaCalculada.plusWeeks(1);
        }

        if (!sesionesNuevas.isEmpty()) {
            sesionRepository.saveAll(sesionesNuevas);
        }
    }

    private DayOfWeek mapearDiaSemana(String dia) {
        switch (dia.toUpperCase()) {
            case "LUNES": return DayOfWeek.MONDAY;
            case "MARTES": return DayOfWeek.TUESDAY;
            case "MIERCOLES": 
            case "MIÉRCOLES": return DayOfWeek.WEDNESDAY;
            case "JUEVES": return DayOfWeek.THURSDAY;
            case "VIERNES": return DayOfWeek.FRIDAY;
            case "SABADO":
            case "SÁBADO": return DayOfWeek.SATURDAY;
            case "DOMINGO": return DayOfWeek.SUNDAY;
            default: throw new RuntimeException("Día de semana no válido: " + dia);
        }
    }

    @Auditable(accion = "ACTUALIZACION", tabla = "tb_horario")
    @Transactional
    public HorarioDTO actualizarHorario(Integer id, HorarioCreateDTO dto) {
        Horario horario = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        return registrarDatosHorario(horario, dto);
    }

    @Auditable(accion = "ESTADO", tabla = "tb_horario")
    @Transactional
    public void cambiarEstadoHorario(Integer id) {
        Horario horario = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setActivo(!horario.getActivo());
        horarioRepository.save(horario);
    }

    private CursoDTO registrarDatosCurso(Curso curso, CursoCreateDTO dto) {
        curso.setNombreCurso(dto.getNombreCurso());
        curso.setCreditos(dto.getCreditos());
        curso.setCupoMaximo(dto.getCupoMaximo());
        curso.setCupoActual(dto.getCupoActual());
        
        curso.setCarrera(carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada")));
        
        curso.setCiclo(cicloRepository.findById(dto.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado")));
        
        curso.setProfesor(usuarioRepository.findById(dto.getIdProfesor())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado")));

        if (dto.getIdRequisito() != null && dto.getIdRequisito() > 0) {
            if (curso.getIdCurso() != null && curso.getIdCurso().equals(dto.getIdRequisito())) {
                throw new RuntimeException("Un curso no puede ser requisito de sí mismo.");
            }
            Curso requisito = cursoRepository.findById(dto.getIdRequisito())
                    .orElseThrow(() -> new RuntimeException("Curso requisito no encontrado"));
            curso.setCursoRequisito(requisito);
        } else {
            curso.setCursoRequisito(null);
        }

        return convertirCursoADTO(cursoRepository.save(curso));
    }
    
    private CursoDTO convertirCursoADTO(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setIdCurso(c.getIdCurso());
        dto.setNombreCurso(c.getNombreCurso());
        dto.setCreditos(c.getCreditos());
        dto.setCupoMaximo(c.getCupoMaximo());
        dto.setCupoActual(c.getCupoActual());
        dto.setActivo(c.getActivo());
        
        if(c.getCarrera() != null) {
            dto.setNombreCarrera(c.getCarrera().getNombreCarrera());
            dto.setIdCarrera(c.getCarrera().getIdCarrera());
        }
        if(c.getCiclo() != null) {
            dto.setNombreCiclo(c.getCiclo().getNombreCiclo());
            dto.setIdCiclo(c.getCiclo().getIdCiclo());
        }
        if(c.getProfesor() != null) {
            dto.setNombreProfesor(c.getProfesor().getNombres() + " " + c.getProfesor().getApellidos());
            dto.setIdProfesor(c.getProfesor().getIdUsuario());
        }
        return dto;
    }

    private HorarioDTO registrarDatosHorario(Horario h, HorarioCreateDTO dto) {
        h.setDiaSemana(dto.getDiaSemana());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFin(dto.getHoraFin());
        
        if (dto.getIdAula() == null) throw new RuntimeException("Debe seleccionar un aula.");
        if (dto.getIdCurso() == null) throw new RuntimeException("Debe seleccionar un curso.");

        h.setAula(aulaRepository.findById(dto.getIdAula()).orElseThrow(() -> new RuntimeException("Aula no encontrada")));
        h.setCurso(cursoRepository.findById(dto.getIdCurso()).orElseThrow(() -> new RuntimeException("Curso no encontrado")));
        
        return convertirHorarioADTO(horarioRepository.save(h));
    }

    private HorarioDTO convertirHorarioADTO(Horario h) {
        HorarioDTO dto = new HorarioDTO();
        dto.setIdHorario(h.getIdHorario());
        dto.setDiaSemana(h.getDiaSemana());
        dto.setHoraInicio(h.getHoraInicio());
        dto.setHoraFin(h.getHoraFin());
        dto.setActivo(h.getActivo());
        
        if(h.getAula() != null) {
            dto.setNombreAula(h.getAula().getDescripcion());
            dto.setIdAula(h.getAula().getIdAula());
        }
        if(h.getCurso() != null) {
            dto.setNombreCurso(h.getCurso().getNombreCurso());
            dto.setIdCurso(h.getCurso().getIdCurso());
        }
        return dto;
    }

    private int compararCercaniaDia(Horario h1, Horario h2) {
        String diaStr1 = h1.getDiaSemana().toUpperCase().trim();
        String diaStr2 = h2.getDiaSemana().toUpperCase().trim();
        int hoy = LocalDate.now().getDayOfWeek().getValue();
        int valorDia1 = DIAS_SEMANA.getOrDefault(diaStr1, 8);
        int valorDia2 = DIAS_SEMANA.getOrDefault(diaStr2, 8);
        int dist1 = (valorDia1 - hoy + 7) % 7;
        int dist2 = (valorDia2 - hoy + 7) % 7;
        if (dist1 == 0 && dist2 == 0) {
            return h1.getHoraInicio().compareTo(h2.getHoraInicio());
        }
        return Integer.compare(dist1, dist2);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private String generarSiglas(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "N/A";
        return Arrays.stream(nombre.split(" "))
                .filter(w -> w.length() > 2)
                .map(w -> w.substring(0, 1))
                .limit(2)
                .collect(Collectors.joining("")).toUpperCase();
    }
}