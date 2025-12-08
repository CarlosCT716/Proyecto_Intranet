package com.cibertec.intranet.matricula.model;

import com.cibertec.intranet.academico.model.Curso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_detalle_matricula")
@Data
public class DetalleMatricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_matricula")
    @JsonIgnore
    private Matricula matricula;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;
}