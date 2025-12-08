package com.cibertec.intranet.profesor.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NotaDTO {
    private Integer idNota;
    private Integer idAlumno;
    private String nombreAlumno;
    private BigDecimal nota1;
    private BigDecimal nota2;
    private BigDecimal nota3;
    private BigDecimal examenFinal;
    private BigDecimal promedioFinal;
}