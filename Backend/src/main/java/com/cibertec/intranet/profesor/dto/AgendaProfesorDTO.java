package com.cibertec.intranet.profesor.dto;

import lombok.Data;

@Data
public class AgendaProfesorDTO {
    private Integer idCurso;
    private String nombreCurso;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String aula;
    private String modalidad;
    private String color;
}