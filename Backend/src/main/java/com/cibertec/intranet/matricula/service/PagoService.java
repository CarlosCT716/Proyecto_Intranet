package com.cibertec.intranet.matricula.service;

import com.cibertec.intranet.matricula.dto.PagoDTO;
import com.cibertec.intranet.matricula.model.Matricula;
import com.cibertec.intranet.matricula.model.Pago;
import com.cibertec.intranet.matricula.repository.EstadoPagoRepository;
import com.cibertec.intranet.matricula.repository.MatriculaRepository;
import com.cibertec.intranet.matricula.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepo;
    private final MatriculaRepository matriculaRepo;
    private final EstadoPagoRepository estadoPagoRepo;

    public List<PagoDTO> listarPagosPorAlumno(Integer idAlumno) {
        Matricula matricula = obtenerMatriculaActiva(idAlumno);
        return pagoRepo.findByMatriculaIdMatricula(matricula.getIdMatricula())
                .stream().map(this::convertirDTO).collect(Collectors.toList());
    }

    public List<PagoDTO> listarPagosPendientes(Integer idAlumno) {
        Matricula matricula = obtenerMatriculaActiva(idAlumno);
        LocalDate finDeMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        return pagoRepo.buscarPendientesVisibles(matricula.getIdMatricula(), 1, finDeMes)
                .stream().map(this::convertirDTO).collect(Collectors.toList());
    }
    @Transactional
    public void realizarPago(Integer idPago) {
        Pago pago = pagoRepo.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstadoPago(estadoPagoRepo.findById(2).orElseThrow());
        pago.setFechaPago(LocalDate.now());

        pagoRepo.save(pago);
    }

    private Matricula obtenerMatriculaActiva(Integer idAlumno) {
        return matriculaRepo.findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(idAlumno)
                .orElseThrow(() -> new RuntimeException("Sin matrícula activa"));
    }

    private PagoDTO convertirDTO(Pago p) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(p.getIdPago());
        dto.setConcepto(p.getConcepto());
        dto.setMonto(p.getMonto());
        dto.setFechaVencimiento(p.getFechaVencimiento());

        if(p.getFechaPago() != null) {
            dto.setFechaPago(p.getFechaPago());
        }

        if (p.getEstadoPago() != null) {
            dto.setEstado(p.getEstadoPago().getDescripcion());
        }

        return dto;
    }
}