package com.cibertec.intranet.profesor.dto;

import lombok.Data;
import java.util.List;

@Data
public class TeacherDashboardDTO {
    private List<CursoAsignadoDTO> cursos;
    private List<ClaseHoyDTO> agendaHoy;
    private SesionPendienteDTO asistenciaPendiente;
    private List<AvanceNotasDTO> cargaNotas;

    @Data
    public static class CursoAsignadoDTO {
        private Integer idCurso;
        private String nombre;
        private String seccion;
        private String aula;
        private String icon;
        private String colorBg;
        private String colorTxt;
    }

    @Data
    public static class ClaseHoyDTO {
        private String horario;
        private String curso;
    }

    @Data
    public static class SesionPendienteDTO {
        private Integer idSesion;
        private String curso;
        private String fecha;
        private String estado;
    }

    @Data
    public static class AvanceNotasDTO {
        private String curso;
        private String evaluacion;
        private Integer porcentaje;
        private String estado;
    }
}