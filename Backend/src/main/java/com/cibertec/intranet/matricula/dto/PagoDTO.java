package com.cibertec.intranet.matricula.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagoDTO {
    private Integer idPago;
    private String concepto;
    private BigDecimal monto;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private String estado;
}