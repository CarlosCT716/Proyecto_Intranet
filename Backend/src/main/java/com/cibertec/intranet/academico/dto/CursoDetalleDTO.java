package com.cibertec.intranet.academico.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CursoDetalleDTO {
    private String nombreCurso;
    private BigDecimal nota1;
    private BigDecimal nota2;
    private BigDecimal nota3;
    private BigDecimal examenFinal;
    private BigDecimal promedio;
    private String porcentajeAsistencia;
    private List<AsistenciaDTO> historialAsistencia;

    @Data
    public static class AsistenciaDTO {
        private String sesion;
        private LocalDate fecha;
        private String tema;
        private String estado;
    }
}