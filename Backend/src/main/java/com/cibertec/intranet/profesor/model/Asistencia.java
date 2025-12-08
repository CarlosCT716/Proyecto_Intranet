package com.cibertec.intranet.profesor.model;

import com.cibertec.intranet.profesor.model.Sesion;
import com.cibertec.intranet.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_asistencia")
@Data
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Integer idAsistencia;

    @ManyToOne
    @JoinColumn(name = "id_sesion", nullable = false)
    private Sesion sesion;

    @ManyToOne
    @JoinColumn(name = "id_alumno", nullable = false)
    private Usuario alumno;

    @Column(name = "id_estado", nullable = false)
    private Integer idEstado;

    private String observacion;
}