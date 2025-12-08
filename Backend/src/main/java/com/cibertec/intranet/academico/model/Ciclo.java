package com.cibertec.intranet.academico.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_ciclo")
@Data
public class Ciclo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCiclo;

    @Column(name = "nombre_ciclo")
    private String nombreCiclo;
}