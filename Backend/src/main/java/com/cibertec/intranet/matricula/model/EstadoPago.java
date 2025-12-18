package com.cibertec.intranet.matricula.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="tb_estado_pago")
@Data
public class EstadoPago {

    @Id
    @Column(name = "id_estado_pago")
    private Integer idEstadoPago;

    @Column(name = "descripcion")
    private String descripcion;
}