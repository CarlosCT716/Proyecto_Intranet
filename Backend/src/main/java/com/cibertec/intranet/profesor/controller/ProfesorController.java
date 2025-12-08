package com.cibertec.intranet.profesor.controller;

import com.cibertec.intranet.profesor.dto.NotaDTO;
import com.cibertec.intranet.profesor.dto.AsistenciaRequestDTO;
import com.cibertec.intranet.profesor.dto.SesionDTO;
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


    @GetMapping("/curso/{idCurso}/notas")
    public ResponseEntity<List<NotaDTO>> listarNotas(@PathVariable Integer idCurso) {
        return ResponseEntity.ok(profesorService.listarNotasPorCurso(idCurso));
    }

    @PutMapping("/notas")
    public ResponseEntity<NotaDTO> actualizarNota(@RequestBody NotaDTO dto) {
        return ResponseEntity.ok(profesorService.actualizarNota(dto));
    }


    @GetMapping("/curso/{idCurso}/sesiones")
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