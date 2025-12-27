package com.cibertec.intranet.academico.controller;

import com.cibertec.intranet.academico.dto.*;
import com.cibertec.intranet.academico.model.*;
import com.cibertec.intranet.academico.service.AcademicoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academico")
@RequiredArgsConstructor
public class AcademicoController {

    private final AcademicoService academicoService;

    @GetMapping("/alumno/{id}/dashboard")
    public ResponseEntity<DashboardDTO> obtenerDashboard(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerDashboardAlumno(id));
    }
    @GetMapping("/alumno/{id}/cursos-matriculados")
    public ResponseEntity<List<CursoMatriculadoDTO>> listarCursosAlumno(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.listarCursosMatriculados(id));
    }
    @GetMapping("/alumno/{id}/cursos")
    public ResponseEntity<List<CursoMatriculadoDTO>> listarCursos(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.listarCursosMatriculados(id));
    }

    @GetMapping("/alumno/{idAlumno}/curso/{idCurso}/detalle")
    public ResponseEntity<CursoDetalleDTO> verDetalleCurso(
            @PathVariable Integer idAlumno,
            @PathVariable Integer idCurso) {
        return ResponseEntity.ok(academicoService.obtenerDetalleCurso(idAlumno, idCurso));
    }
    @GetMapping("/alumno/{id}/horario")
    public ResponseEntity<List<HorarioAlumnoDTO>> obtenerHorarioAlumno(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerHorarioAlumno(id));
    }
    
    @GetMapping("/carreras")
    public ResponseEntity<List<Carrera>> listarCarreras() {
        return ResponseEntity.ok(academicoService.listarCarreras());
    }


    @PostMapping("/carreras")
    public ResponseEntity<Carrera> crearCarrera(@RequestBody Carrera carrera) {
        return ResponseEntity.ok(academicoService.guardarCarrera(carrera));
    }

    @PutMapping("/carreras/{id}")
    public ResponseEntity<Carrera> actualizarCarrera(@PathVariable Integer id, @RequestBody Carrera carrera) {
        carrera.setIdCarrera(id);
        return ResponseEntity.ok(academicoService.guardarCarrera(carrera));
    }
    @GetMapping("/carreras/{id}")
    public ResponseEntity<Carrera> obtenerCarrera(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerCarrera(id));
    }

    @GetMapping("/ciclos/{id}")
    public ResponseEntity<Ciclo> obtenerCiclo(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerCiclo(id));
    }

    @GetMapping("/aulas/{id}")
    public ResponseEntity<Aula> obtenerAula(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerAula(id));
    }


    @GetMapping("/ciclos")
    public ResponseEntity<List<Ciclo>> listarCiclos() {
        return ResponseEntity.ok(academicoService.listarCiclos());
    }



    @PostMapping("/ciclos")
    public ResponseEntity<Ciclo> crearCiclo(@RequestBody Ciclo ciclo) {
        return ResponseEntity.ok(academicoService.guardarCiclo(ciclo));
    }

    @PutMapping("/ciclos/{id}")
    public ResponseEntity<Ciclo> actualizarCiclo(@PathVariable Integer id, @RequestBody Ciclo ciclo) {
        ciclo.setIdCiclo(id);
        return ResponseEntity.ok(academicoService.guardarCiclo(ciclo));
    }


    @GetMapping("/aulas")
    public ResponseEntity<List<Aula>> listarAulas() {
        return ResponseEntity.ok(academicoService.listarAulas());
    }

    @GetMapping("/aulas/activas")
    public ResponseEntity<List<Aula>> listarAulasActivas() {
        return ResponseEntity.ok(academicoService.listarAulasActivas());
    }

    @PostMapping("/aulas")
    public ResponseEntity<Aula> crearAula(@RequestBody Aula aula) {
        return ResponseEntity.ok(academicoService.guardarAula(aula));
    }

    @PutMapping("/aulas/{id}")
    public ResponseEntity<Aula> actualizarAula(@PathVariable Integer id, @RequestBody Aula aula) {
        aula.setIdAula(id);
        return ResponseEntity.ok(academicoService.guardarAula(aula));
    }

    @PatchMapping("/aulas/estado/{id}")
    public ResponseEntity<Void> cambiarEstadoAula(@PathVariable Integer id) {
        academicoService.cambiarEstadoAula(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(academicoService.listarCursos());
    }

    @GetMapping("/cursos/activos")
    public ResponseEntity<List<CursoDTO>> listarCursosActivos() {
        return ResponseEntity.ok(academicoService.listarCursosActivos());
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> obtenerCurso(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerCurso(id));
    }
    @GetMapping("/cursos/filtro")
    public ResponseEntity<List<CursoDTO>> filtrarCursos(
            @RequestParam Integer idCarrera,
            @RequestParam Integer idCiclo) {
        return ResponseEntity.ok(academicoService.filtrarCursos(idCarrera, idCiclo));
    }

    @PostMapping("/cursos")
    public ResponseEntity<CursoDTO> crearCurso(@RequestBody CursoCreateDTO dto) {
        return ResponseEntity.ok(academicoService.guardarCurso(dto));
    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> actualizarCurso(@PathVariable Integer id, @RequestBody CursoCreateDTO dto) {
        return ResponseEntity.ok(academicoService.actualizarCurso(id, dto));
    }

    @PatchMapping("/cursos/estado/{id}")
    public ResponseEntity<Void> cambiarEstadoCurso(@PathVariable Integer id) {
        academicoService.cambiarEstadoCurso(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioDTO>> listarHorarios() {
        return ResponseEntity.ok(academicoService.listarHorarios());
    }

    @GetMapping("/horarios/activos")
    public ResponseEntity<List<HorarioDTO>> listarHorariosActivos() {
        return ResponseEntity.ok(academicoService.listarHorariosActivos());
    }

    @GetMapping("/horarios/{id}")
    public ResponseEntity<HorarioDTO> obtenerHorario(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerHorario(id));
    }

    @PostMapping("/horarios")
    public ResponseEntity<HorarioDTO> crearHorario(@RequestBody HorarioCreateDTO dto) {
        return ResponseEntity.ok(academicoService.crearHorario(dto));
    }

    @PutMapping("/horarios/{id}")
    public ResponseEntity<HorarioDTO> actualizarHorario(@PathVariable Integer id, @RequestBody HorarioCreateDTO dto) {
        return ResponseEntity.ok(academicoService.actualizarHorario(id, dto));
    }

    @PatchMapping("/horarios/estado/{id}")
    public ResponseEntity<Void> cambiarEstadoHorario(@PathVariable Integer id) {
        academicoService.cambiarEstadoHorario(id);
        return ResponseEntity.ok().build();
    }
}