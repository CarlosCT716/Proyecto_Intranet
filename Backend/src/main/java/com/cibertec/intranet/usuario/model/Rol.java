package com.cibertec.intranet.usuario.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_rol")
@Data
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre_rol", nullable = false)
    private String nombreRol;
}