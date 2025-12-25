package com.cibertec.intranet.auditoria.controller;

import com.cibertec.intranet.auditoria.dto.AuditoriaDTO;
import com.cibertec.intranet.auditoria.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaDTO>> listarAuditoria() {
        return ResponseEntity.ok(auditoriaService.listarTodo());
    }
}