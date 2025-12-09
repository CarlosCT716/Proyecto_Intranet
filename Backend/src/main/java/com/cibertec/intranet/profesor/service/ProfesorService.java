    package com.cibertec.intranet.profesor.service;

    import com.cibertec.intranet.academico.model.Curso;
    import com.cibertec.intranet.auditoria.annotation.Auditable;
    import com.cibertec.intranet.profesor.model.Sesion;
    import com.cibertec.intranet.academico.repository.CursoRepository;
    import com.cibertec.intranet.profesor.repository.SesionRepository;
    import com.cibertec.intranet.profesor.dto.*;
    import com.cibertec.intranet.profesor.model.Asistencia;
    import com.cibertec.intranet.profesor.model.Nota;
    import com.cibertec.intranet.profesor.repository.AsistenciaRepository;
    import com.cibertec.intranet.profesor.repository.NotaRepository;
    import com.cibertec.intranet.usuario.model.Usuario;
    import com.cibertec.intranet.usuario.repository.UsuarioRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
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

        public List<Curso> listarCursosAsignados(Integer idProfesor, Integer idCiclo, Integer idCarrera) {
            List<Curso> cursos = cursoRepository.findByProfesor_IdUsuario(idProfesor);

            return cursos.stream()
                    .filter(c -> idCiclo == null || c.getCiclo().getIdCiclo().equals(idCiclo))
                    .filter(c -> idCarrera == null || c.getCarrera().getIdCarrera().equals(idCarrera))
                    .collect(Collectors.toList());
        }
        // GESTIÓN DE NOTAS
        public List<NotaDTO> listarNotasPorCurso(Integer idCurso) {
            return notaRepository.findByDetalleMatricula_Curso_IdCurso(idCurso).stream()
                    .map(this::convertirNotaADTO)
                    .collect(Collectors.toList());
        }

        @Auditable(accion = "ACTUALZACION", tabla = "tb_notas")
        @Transactional
        public NotaDTO actualizarNota(NotaDTO dto) {
            Nota nota = notaRepository.findById(dto.getIdNota())
                    .orElseThrow(() -> new RuntimeException("Registro de notas no encontrado"));

            nota.setNota1(dto.getNota1());
            nota.setNota2(dto.getNota2());
            nota.setNota3(dto.getNota3());
            nota.setExamenFinal(dto.getExamenFinal());

            return convertirNotaADTO(notaRepository.save(nota));
        }

        // GESTIÓN DE SESIONES (Clases por fecha)
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

        @Auditable(accion = "CREACION", tabla = "tb_sesion")
        @Transactional
        public SesionDTO crearSesion(SesionDTO dto) {
            Curso curso = cursoRepository.findById(dto.getIdCurso())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            Sesion sesion = new Sesion();
            sesion.setCurso(curso);
            sesion.setFecha(dto.getFecha());
            sesion.setTemaTratado(dto.getTemaTratado());
            sesion.setEstadoSesion("PROGRAMADA");

            Sesion guardada = sesionRepository.save(sesion);
            dto.setIdSesion(guardada.getIdSesion());
            dto.setEstado("PROGRAMADA");
            return dto;
        }

        //Asistencia
        @Auditable(accion = "CREACION", tabla = "tb_asistencia")
        @Transactional
        public void registrarAsistenciaMasiva(Integer idSesion, List<AsistenciaDTO> listaAsistencia) {
            Sesion sesion = sesionRepository.findById(idSesion)
                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

            sesion.setEstadoSesion("FINALIZADA");
            sesionRepository.save(sesion);

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

        // Mapper Auxiliar para Notas
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