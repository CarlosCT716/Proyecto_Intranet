package com.cibertec.intranet.profesor.dto;

import lombok.Data;

@Data
public class AsistenciaDetalleDTO {
    private Integer idAlumno;
    private String nombreAlumno;
    private Integer idEstado;
    private String observacion;
}