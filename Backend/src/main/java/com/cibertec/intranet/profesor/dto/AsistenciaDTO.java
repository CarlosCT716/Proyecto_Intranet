package com.cibertec.intranet.profesor.dto;

import lombok.Data;

@Data
public class AsistenciaDTO {
    private Integer idAlumno;
    private Integer estado;
    private String observacion;
}