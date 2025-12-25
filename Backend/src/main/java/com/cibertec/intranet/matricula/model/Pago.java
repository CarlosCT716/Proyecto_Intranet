package com.cibertec.intranet.matricula.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_pago")
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name = "id_matricula")
    private Matricula matricula;

    private String concepto;
    private BigDecimal monto;
    private LocalDate fechaVencimiento;

    @ManyToOne
    @JoinColumn(name = "id_estado_pago")
    private EstadoPago estadoPago;

    private LocalDate fechaPago;
}