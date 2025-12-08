package com.cibertec.intranet.matricula.controller;

import com.cibertec.intranet.matricula.dto.MatriculaDTO;
import com.cibertec.intranet.matricula.model.Matricula;
import com.cibertec.intranet.matricula.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService _matricula;

    @PostMapping
    public ResponseEntity<?> registrarMatricula(@RequestBody MatriculaDTO dto) {
        try {
            Matricula nuevaMatricula = _matricula.registrarMatricula(dto);
            return ResponseEntity.ok(nuevaMatricula);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error en matrícula: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Matricula>> listarMatriculas() {
        return ResponseEntity.ok(_matricula.listarTodas());
    }
}