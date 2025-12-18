package com.cibertec.intranet.academico.dto;

import lombok.Data;

@Data
public class CursoDTO {
    private Integer idCurso;
    private String nombreCurso;
    private Integer creditos;
    private Integer cupoMaximo;
    private Integer cupoActual;
    private String nombreCarrera;
    private String nombreCiclo;
    private String nombreProfesor;
}