package com.cibertec.intranet.academico.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_carrera")
@Data
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrera;

    @Column(name = "nombre_carrera")
    private String nombreCarrera;

    private String descripcion;
}