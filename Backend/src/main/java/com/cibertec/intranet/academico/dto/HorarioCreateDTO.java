package com.cibertec.intranet.academico.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class HorarioCreateDTO {
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer idAula;
    private Integer idCurso;
    private LocalDate fechaInicio;
}