package com.cibertec.intranet.auditoria.service;

import com.cibertec.intranet.auditoria.model.Auditoria;
import com.cibertec.intranet.auditoria.repository.AuditoriaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarAccion(String accion, String tabla, Object detalleNuevo, Object detalleAnterior, Integer idUsuario, String ip) {
        Auditoria auditoria = new Auditoria();
        auditoria.setAccion(accion);
        auditoria.setTablaAfectada(tabla);
        auditoria.setIdUsuario(idUsuario);
        auditoria.setIpOrigen(ip);
        auditoria.setFecha(LocalDateTime.now());

        try {

            if (detalleNuevo != null) {
                auditoria.setDetalleNuevo(objectMapper.writeValueAsString(detalleNuevo));
            }
            if (detalleAnterior != null) {
                auditoria.setDetalleAnterior(objectMapper.writeValueAsString(detalleAnterior));
            }
        } catch (JsonProcessingException e) {
            auditoria.setDetalleNuevo("ERROR AL SERIALIZAR OBJETO: " + detalleNuevo.getClass().getSimpleName());
        }

        auditoriaRepository.save(auditoria);
    }
}