package com.cibertec.intranet.academico.model;

import com.cibertec.intranet.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_curso")
@Data
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;

    @Column(name = "nombre_curso")
    private String nombreCurso;

    private Integer creditos;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @Column(name = "cupo_actual")
    private Integer cupoActual;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @ManyToOne
    @JoinColumn(name = "id_ciclo")
    private Ciclo ciclo;

    @ManyToOne
    @JoinColumn(name = "id_profesor")
    private Usuario profesor;

    @ManyToOne
    @JoinColumn(name = "id_requisito") 
    private Curso cursoRequisito;

    @Column(name = "activo")
    private Boolean activo = true;
}