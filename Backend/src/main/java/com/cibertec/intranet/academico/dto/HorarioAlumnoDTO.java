package com.cibertec.intranet.academico.dto;

import lombok.Data;

@Data
public class HorarioAlumnoDTO {
    private Integer idCurso;
    private String nombreCurso;
    private String seccion; // Ej: T3WN
    private String dia;     // LUNES, MARTES...
    private String horaInicio; // "08:00:00"
    private String horaFin;    // "10:00:00"
    private String aula;
    private String profesor;
    private String color;   // Para darle colorcitos en el front
}