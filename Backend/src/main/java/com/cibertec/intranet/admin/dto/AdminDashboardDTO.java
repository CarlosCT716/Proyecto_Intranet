package com.cibertec.intranet.admin.dto;

import lombok.Data;
import java.util.List;

@Data
public class AdminDashboardDTO {
    private Long totalEstudiantes;
    private Long docentesActivos;
    private Long incidenciasIA;
    private Long auditoriaHoy;
    
    private List<ChartDataDTO> estudiantesPorCarrera;
    private List<ChartDataDTO> distribucionUsuarios;
    private List<AuditoriaResumenDTO> ultimosMovimientos;

    @Data
    public static class ChartDataDTO {
        private String label;
        private Long value;

        public ChartDataDTO(String label, Long value) {
            this.label = label;
            this.value = value;
        }
    }

    @Data
    public static class AuditoriaResumenDTO {
        private String usuario;
        private String accion;
        private String tabla;
        private String fecha;
        private String ip;
    }
}