package com.cibertec.intranet.matricula.model;

import com.cibertec.intranet.academico.model.Carrera;
import com.cibertec.intranet.academico.model.Ciclo;
import com.cibertec.intranet.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// Matricula.java
@Entity
@Table(name = "tb_matricula")
@Data
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMatricula;

    @ManyToOne
    @JoinColumn(name = "id_alumno")
    private Usuario alumno;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @ManyToOne
    @JoinColumn(name = "id_ciclo")
    private Ciclo ciclo;

    private String periodo; // Ej: "2025-1"
    private LocalDateTime fechaMatricula;

    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL)
    private List<DetalleMatricula> detalles;
}

