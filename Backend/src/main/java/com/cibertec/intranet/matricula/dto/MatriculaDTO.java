package com.cibertec.intranet.matricula.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatriculaDTO {
    private Integer idAlumno;
    private Integer idCarrera;
    private Integer idCiclo;
    private String periodo;
    private List<Integer> idCursos;

}