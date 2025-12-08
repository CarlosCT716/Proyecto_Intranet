package com.cibertec.intranet.profesor.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SesionDTO {
    private Integer idSesion;
    private Integer idCurso;
    private LocalDate fecha;
    private String temaTratado;
    private String estado;
}