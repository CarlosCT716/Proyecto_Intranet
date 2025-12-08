package com.cibertec.intranet.asistente.controller;

import com.cibertec.intranet.asistente.dto.ConsultaDTO;
import com.cibertec.intranet.asistente.dto.RespuestaDTO;
import com.cibertec.intranet.asistente.service.GeminiService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/asistente")
@RequiredArgsConstructor
public class AsistenteController {

    private final GeminiService geminiService;

    @PostMapping("/chat")
    public ResponseEntity<RespuestaDTO> chatear(@RequestBody ConsultaDTO consulta) {

        if (consulta.getMensaje() == null || consulta.getMensaje().isBlank()) {
            return ResponseEntity.badRequest().body(new RespuestaDTO(null,"Escribe una pregunta"));
        }

        String respuestaIA = geminiService.procesarConsulta(consulta.getMensaje(), consulta.getUserId());

        return ResponseEntity.ok(new RespuestaDTO(respuestaIA,null));
    }
}