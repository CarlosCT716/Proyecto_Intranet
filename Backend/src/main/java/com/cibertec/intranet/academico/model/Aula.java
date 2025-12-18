package com.cibertec.intranet.academico.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_aula")
@Data
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAula;
    private String descripcion;
    @Column(name = "activo")
    private Boolean activo = true;
    @Column(name = "aforo_actual")
    private Integer aforoActual;
     @Column(name = "aforo_maximo")
    private Integer aforoMaximo;
}
