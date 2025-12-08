package com.cibertec.intranet.academico.service;

import com.cibertec.intranet.academico.dto.CursoCreateDTO;
import com.cibertec.intranet.academico.dto.CursoDTO;
import com.cibertec.intranet.academico.dto.HorarioCreateDTO;
import com.cibertec.intranet.academico.dto.HorarioDTO;
import com.cibertec.intranet.academico.model.*;
import com.cibertec.intranet.academico.repository.*;
import com.cibertec.intranet.auditoria.annotation.Auditable;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public List<Carrera> listarCarreras() { return carreraRepository.findAll(); }

    public Carrera obtenerCarrera(Integer id) {
        return carreraRepository.findById(id).orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
    }

    @Transactional
    public Carrera guardarCarrera(Carrera carrera) {
        return carreraRepository.save(carrera);
    }


    public List<Ciclo> listarCiclos() { return cicloRepository.findAll(); }

    public Ciclo obtenerCiclo(Integer id) {
        return cicloRepository.findById(id).orElseThrow(() -> new RuntimeException("Ciclo no encontrado"));
    }

    @Transactional
    public Ciclo guardarCiclo(Ciclo ciclo) {
        return cicloRepository.save(ciclo);
    }

    //GESTIÓN DE AULAS
    public List<Aula> listarAulas() {
        return aulaRepository.findAll();
    }

    public Aula obtenerAula(Integer id) {
        return aulaRepository.findById(id).orElseThrow(() -> new RuntimeException("Aula no encontrada"));
    }

    @Auditable(accion = "CREACION", tabla = "tb_aula")
    @Transactional
    public Aula guardarAula(Aula aula) {
        if(aula.getIdAula() == null) aula.setActivo(true);
        return aulaRepository.save(aula);
    }

    @Transactional
    public void cambiarEstadoAula(Integer id) {
        Aula aula = obtenerAula(id);
        aula.setActivo(!aula.getActivo());
        aulaRepository.save(aula);
    }


    //GESTIÓN DE CURSOS

    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertirCursoADTO)
                .collect(Collectors.toList());
    }

    public CursoDTO obtenerCurso(Integer id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        return convertirCursoADTO(curso);
    }

    @Transactional
    public CursoDTO guardarCurso(CursoCreateDTO dto) {
        Curso curso = new Curso();
        curso.setActivo(true);
        return registrarDatosCurso(curso, dto);
    }

    @Transactional
    public CursoDTO actualizarCurso(Integer id, CursoCreateDTO dto) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        return registrarDatosCurso(curso, dto);
    }

    @Transactional
    public void cambiarEstadoCurso(Integer id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        curso.setActivo(!curso.getActivo());
        cursoRepository.save(curso);
    }

    //GESTIÓN DE HORARIOS

    public List<HorarioDTO> listarHorarios() {
        return horarioRepository.findAll().stream()
                .map(this::convertirHorarioADTO)
                .collect(Collectors.toList());
    }

    public HorarioDTO obtenerHorario(Integer id) {
        Horario horario = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        return convertirHorarioADTO(horario);
    }

    @Transactional
    public HorarioDTO crearHorario(HorarioCreateDTO dto) {
        Horario horario = new Horario();
        horario.setActivo(true);
        return registrarDatosHorario(horario, dto);
    }

    @Transactional
    public HorarioDTO actualizarHorario(Integer id, HorarioCreateDTO dto) {
        Horario horario = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        return registrarDatosHorario(horario, dto);
    }

    @Transactional
    public void cambiarEstadoHorario(Integer id) {
        Horario horario = horarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setActivo(!horario.getActivo());
        horarioRepository.save(horario);
    }

    // MÉTODOS AUXILIARES (Mappers & Helpers)
    private CursoDTO registrarDatosCurso(Curso curso, CursoCreateDTO dto) {
        curso.setNombreCurso(dto.getNombreCurso());
        curso.setCreditos(dto.getCreditos());
        curso.setCupoMaximo(dto.getCupoMaximo());

        curso.setCarrera(carreraRepository.findById(dto.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada")));
        curso.setCiclo(cicloRepository.findById(dto.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo no encontrado")));
        curso.setProfesor(usuarioRepository.findById(dto.getIdProfesor())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado")));

        return convertirCursoADTO(cursoRepository.save(curso));
    }

    private CursoDTO convertirCursoADTO(Curso c) {
        CursoDTO dto = new CursoDTO();
        dto.setIdCurso(c.getIdCurso());
        dto.setNombreCurso(c.getNombreCurso());
        dto.setCreditos(c.getCreditos());
        dto.setCupoMaximo(c.getCupoMaximo());

        if(c.getCarrera() != null) dto.setNombreCarrera(c.getCarrera().getNombreCarrera());
        if(c.getCiclo() != null) dto.setNombreCiclo(c.getCiclo().getNombreCiclo());
        if(c.getProfesor() != null) {
            dto.setNombreProfesor(c.getProfesor().getNombres() + " " + c.getProfesor().getApellidos());
        }
        return dto;
    }

    private HorarioDTO registrarDatosHorario(Horario h, HorarioCreateDTO dto) {
        h.setDiaSemana(dto.getDiaSemana());
        h.setHoraInicio(dto.getHoraInicio());
        h.setHoraFin(dto.getHoraFin());

        h.setAula(aulaRepository.findById(dto.getIdAula())
                .orElseThrow(() -> new RuntimeException("Aula no encontrada")));
        h.setCurso(cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado")));

        return convertirHorarioADTO(horarioRepository.save(h));
    }

    private HorarioDTO convertirHorarioADTO(Horario h) {
        HorarioDTO dto = new HorarioDTO();
        dto.setIdHorario(h.getIdHorario());
        dto.setDiaSemana(h.getDiaSemana());
        dto.setHoraInicio(h.getHoraInicio());
        dto.setHoraFin(h.getHoraFin());
        if(h.getAula() != null) dto.setNombreAula(h.getAula().getDescripcion());
        if(h.getCurso() != null) dto.setNombreCurso(h.getCurso().getNombreCurso());
        return dto;
    }

}