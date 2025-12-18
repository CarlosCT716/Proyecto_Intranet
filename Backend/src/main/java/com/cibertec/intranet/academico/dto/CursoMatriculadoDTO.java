package com.cibertec.intranet.academico.dto;

import lombok.Data;

@Data
public class CursoMatriculadoDTO {
    private Integer idCurso;
    private String nombreCurso;
    private String seccion;
    private String nombreProfesor;
    private String aula;
    private String modalidad;
    private String estado;
}