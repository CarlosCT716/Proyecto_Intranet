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

    // carreras
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



    // ciclos
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


    // aulas
    @GetMapping("/aulas")
    public ResponseEntity<List<Aula>> listarAulas() {
        return ResponseEntity.ok(academicoService.listarAulas());
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

    @PatchMapping("/aulas/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoAula(@PathVariable Integer id) {
        academicoService.cambiarEstadoAula(id);
        return ResponseEntity.ok().build();
    }

    //cursos
    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(academicoService.listarCursos());
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> obtenerCurso(@PathVariable Integer id) {
        return ResponseEntity.ok(academicoService.obtenerCurso(id));
    }

    @PostMapping("/cursos")
    public ResponseEntity<CursoDTO> crearCurso(@RequestBody CursoCreateDTO dto) {
        return ResponseEntity.ok(academicoService.guardarCurso(dto));
    }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> actualizarCurso(@PathVariable Integer id, @RequestBody CursoCreateDTO dto) {
        return ResponseEntity.ok(academicoService.actualizarCurso(id, dto));
    }

    @PatchMapping("/cursos/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoCurso(@PathVariable Integer id) {
        academicoService.cambiarEstadoCurso(id);
        return ResponseEntity.ok().build();
    }

    //horarioa
    @GetMapping("/horarios")
    public ResponseEntity<List<HorarioDTO>> listarHorarios() {
        return ResponseEntity.ok(academicoService.listarHorarios());
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

    @PatchMapping("/horarios/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoHorario(@PathVariable Integer id) {
        academicoService.cambiarEstadoHorario(id);
        return ResponseEntity.ok().build();
    }
}