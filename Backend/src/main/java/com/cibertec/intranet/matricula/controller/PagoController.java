package com.cibertec.intranet.matricula.controller;

import com.cibertec.intranet.matricula.dto.PagoDTO;
import com.cibertec.intranet.matricula.model.Pago;
import com.cibertec.intranet.matricula.repository.PagoRepository;
import com.cibertec.intranet.matricula.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final PagoRepository pagoRepository;
    @GetMapping
    public ResponseEntity<List<Pago>> listarTodosLosPagos() {
        return ResponseEntity.ok(pagoRepository.findAll());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Pago>> listarPagosPendientes() {
        return ResponseEntity.ok(pagoRepository.findByEstadoPago_IdEstadoPago(1)); 
    }
    @GetMapping("/alumno/{id}/historial")
    public ResponseEntity<List<PagoDTO>> listarHistorial(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.listarPagosPorAlumno(id));
    }

    @GetMapping("/alumno/{id}/pendientes")
    public ResponseEntity<List<PagoDTO>> listarPendientes(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.listarPagosPendientes(id));
    }

    @PostMapping("/{idPago}/pagar")
    public ResponseEntity<Void> realizarPago(@PathVariable Integer idPago) {
        pagoService.realizarPago(idPago);
        return ResponseEntity.ok().build();
    }
}