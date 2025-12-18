package com.cibertec.intranet.academico.dto;

import lombok.Data;
import java.util.List;
@Data
public class DashboardDTO {
    private List<CursoResumenDTO> cursos;
    private List<ClaseDTO> proximasClases;
    private EstadoCuentaDTO estadoCuenta;

    @Data
    public static class CursoResumenDTO {
        private String siglas;
        private String nombre;
        private String modalidad;
        private Integer promedio;
    }

    @Data
    public static class ClaseDTO {
        private String horario;
        private String curso;
        private String aula;
        private boolean esVirtual;
    }

    @Data
    public static class EstadoCuentaDTO {
        private String concepto;
        private String vencimiento;
        private Double monto;
        private boolean tieneDeuda;
    }
}