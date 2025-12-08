package com.cibertec.intranet.profesor.model;

import com.cibertec.intranet.matricula.model.DetalleMatricula;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_nota")
@Data
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNota;

    @OneToOne
    @JoinColumn(name = "id_detalle_matricula", nullable = false)
    private DetalleMatricula detalleMatricula;

    @Column(precision = 4, scale = 2)
    private BigDecimal nota1 = BigDecimal.ZERO;

    @Column(precision = 4, scale = 2)
    private BigDecimal nota2 = BigDecimal.ZERO;

    @Column(precision = 4, scale = 2)
    private BigDecimal nota3 = BigDecimal.ZERO;

    @Column(name = "examen_final", precision = 4, scale = 2)
    private BigDecimal examenFinal = BigDecimal.ZERO;

    @Column(name = "promedio_final", insertable = false, updatable = false)
    private BigDecimal promedioFinal;
}