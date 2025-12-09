package com.cibertec.intranet.profesor.controller;

import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.academico.repository.CursoRepository;
import com.cibertec.intranet.profesor.dto.NotaDTO;
import com.cibertec.intranet.profesor.dto.AsistenciaRequestDTO;
import com.cibertec.intranet.profesor.dto.SesionDTO;
import com.cibertec.intranet.profesor.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profesor")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;


    @GetMapping("/cursos/asignados/{idProfesor}")
    public ResponseEntity<List<Curso>> listarCursosDelProfesor(
            @PathVariable Integer idProfesor,
            @RequestParam(required = false) Integer idCiclo,
            @RequestParam(required = false) Integer idCarrera
    ) {
        List<Curso> cursos = profesorService.listarCursosAsignados(
                idProfesor, idCiclo, idCarrera
        );
        return ResponseEntity.ok(cursos);
    }
    @GetMapping("/curso/notas/{idCurso}")
    public ResponseEntity<List<NotaDTO>> listarNotas(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(profesorService.listarNotasPorCurso(idCurso));
    }

    @PutMapping("/notas")
    public ResponseEntity<NotaDTO> actualizarNota(@RequestBody NotaDTO dto) {
        return ResponseEntity.ok(profesorService.actualizarNota(dto));
    }


    @GetMapping("/curso/sesiones/{idCurso}")
    public ResponseEntity<List<SesionDTO>> listarSesiones(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(profesorService.listarSesionesPorCurso(idCurso));
    }

    @PostMapping("/sesiones")
    public ResponseEntity<SesionDTO> crearSesion(@RequestBody SesionDTO dto) {
        return ResponseEntity.ok(profesorService.crearSesion(dto));
    }

    @PostMapping("/asistencia")
    public ResponseEntity<String> registrarAsistencia(@RequestBody AsistenciaRequestDTO request) {
        profesorService.registrarAsistenciaMasiva(request.getIdSesion(), request.getAsistencias());
        return ResponseEntity.ok("Asistencia registrada correctamente");
    }
}