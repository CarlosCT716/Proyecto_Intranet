package com.cibertec.intranet.academico.dto;

import lombok.Data;

@Data
public class CursoCreateDTO {
    private String nombreCurso;
    private Integer creditos;
    private Integer cupoMaximo;
    private Integer cupoActual;
    private Integer idCarrera;
    private Integer idCiclo;
    private Integer idProfesor;
    private Integer idRequisito;
}