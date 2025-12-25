package com.cibertec.intranet.auditoria.dto;

import lombok.Data;

@Data
public class AuditoriaDTO {
    private Integer idAuditoria;
    private String nombreUsuario; 
    private String rolUsuario;  
    private String accion;
    private String tablaAfectada;
    private String detalleAnterior; 
    private String detalleNuevo;   
    private String fecha;
    private String ipOrigen;
}