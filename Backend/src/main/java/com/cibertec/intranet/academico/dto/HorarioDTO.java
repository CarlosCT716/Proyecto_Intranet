package com.cibertec.intranet.academico.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioDTO {
    private Integer idHorario;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String nombreAula;
    private String nombreCurso;
    private Boolean activo;
}