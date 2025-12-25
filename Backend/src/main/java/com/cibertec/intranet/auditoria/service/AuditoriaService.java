package com.cibertec.intranet.auditoria.service;

import com.cibertec.intranet.auditoria.dto.AuditoriaDTO;
import com.cibertec.intranet.auditoria.model.Auditoria;
import com.cibertec.intranet.auditoria.repository.AuditoriaRepository;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarAccion(String accion, String tabla, Object detalleNuevo, Object detalleAnterior, Integer idUsuario, String ip) {
        Auditoria auditoria = new Auditoria();
        auditoria.setAccion(accion);
        auditoria.setTablaAfectada(tabla);
        auditoria.setIpOrigen(ip);
        auditoria.setFecha(LocalDateTime.now());

        if (idUsuario != null) {
            Usuario usuarioRef = usuarioRepository.getReferenceById(idUsuario);
            auditoria.setUsuario(usuarioRef);
        }

        try {
            if (detalleNuevo != null) {
                auditoria.setDetalleNuevo(objectMapper.writeValueAsString(detalleNuevo));
            }
            if (detalleAnterior != null) {
                auditoria.setDetalleAnterior(objectMapper.writeValueAsString(detalleAnterior));
            }
        } catch (JsonProcessingException e) {
            auditoria.setDetalleNuevo("ERROR AL SERIALIZAR OBJETO");
        }

        auditoriaRepository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaDTO> listarTodo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return auditoriaRepository.findAll().stream().map(a -> {
            AuditoriaDTO dto = new AuditoriaDTO();
            dto.setIdAuditoria(a.getIdAuditoria());
            dto.setAccion(a.getAccion());
            dto.setTablaAfectada(a.getTablaAfectada());
            dto.setDetalleAnterior(a.getDetalleAnterior());
            dto.setDetalleNuevo(a.getDetalleNuevo());
            dto.setIpOrigen(a.getIpOrigen());
            dto.setFecha(a.getFecha().format(formatter));

            if (a.getUsuario() != null) {
                dto.setNombreUsuario(a.getUsuario().getUsername());
                if (a.getUsuario().getRol() != null) {
                    dto.setRolUsuario(a.getUsuario().getRol().getNombreRol());
                }
            } else {
                dto.setNombreUsuario("Sistema");
                dto.setRolUsuario("N/A");
            }
            return dto;
        }).sorted((o1, o2) -> o2.getIdAuditoria().compareTo(o1.getIdAuditoria()))
          .collect(Collectors.toList());
    }
}