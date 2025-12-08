package com.cibertec.intranet.profesor.model;

import com.cibertec.intranet.academico.model.Curso;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tb_sesion_clase")
@Data
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Integer idSesion;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "tema_tratado")
    private String temaTratado;

    @Column(name = "observaciones_docente")
    private String observacionesDocente;

    @Column(name = "estado_sesion")
    private String estadoSesion;
}