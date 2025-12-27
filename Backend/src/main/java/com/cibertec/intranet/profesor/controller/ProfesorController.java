package com.cibertec.intranet.profesor.controller;

import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.profesor.dto.*;
import com.cibertec.intranet.profesor.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesor")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping("/dashboard/{idProfesor}")
    public ResponseEntity<TeacherDashboardDTO> obtenerDashboard(@PathVariable Integer idProfesor) {
        return ResponseEntity.ok(profesorService.obtenerDashboardProfesor(idProfesor));
    }

    @GetMapping("/agenda/{idProfesor}")
    public ResponseEntity<List<AgendaProfesorDTO>> obtenerAgenda(@PathVariable Integer idProfesor) {
        return ResponseEntity.ok(profesorService.obtenerAgendaCompleta(idProfesor));
    }

    @GetMapping("/cursos/asignados/{idProfesor}")
    public ResponseEntity<List<Curso>> listarCursosDelProfesor(
            @PathVariable Integer idProfesor,
            @RequestParam(required = false) Integer idCiclo,
            @RequestParam(required = false) Integer idCarrera
    ) {
        List<Curso> cursos = profesorService.listarCursosAsignados(idProfesor, idCiclo, idCarrera);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/curso/notas/{idCurso}")
    public ResponseEntity<List<NotaDTO>> listarNotas(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(profesorService.listarNotasPorCurso(idCurso));
    }

    @PutMapping("/notas/masivo")
    public ResponseEntity<List<NotaDTO>> actualizarNotasMasivo(@RequestBody List<NotaDTO> dtos) {
        return ResponseEntity.ok(profesorService.actualizarNotasMasivo(dtos));
    }

    @GetMapping("/curso/sesiones/{idCurso}")
    public ResponseEntity<List<SesionDTO>> listarSesiones(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(profesorService.listarSesionesPorCurso(idCurso));
    }

    @GetMapping("/sesion/{idSesion}/asistencia")
    public ResponseEntity<List<AsistenciaDetalleDTO>> obtenerAsistenciaSesion(@PathVariable Integer idSesion) {
        return ResponseEntity.ok(profesorService.obtenerDetalleAsistencia(idSesion));
    }

    @PostMapping("/asistencia")
    public ResponseEntity<String> registrarAsistencia(@RequestBody AsistenciaRequestDTO request) {
        profesorService.registrarAsistenciaMasiva(request.getIdSesion(), request.getAsistencias());
        return ResponseEntity.ok("Asistencia guardada correctamente");
    }

    @PatchMapping("/sesion/{idSesion}/finalizar")
    public ResponseEntity<Void> finalizarSesion(@PathVariable Integer idSesion) {
        profesorService.finalizarSesion(idSesion);
        return ResponseEntity.ok().build();
    }

  @PatchMapping("/sesiones/{idSesion}")
    public ResponseEntity<SesionDTO> actualizarTema(@PathVariable Integer idSesion, @RequestBody SesionDTO dto) {
        return ResponseEntity.ok(profesorService.actualizarTema(idSesion, dto));
    }
}