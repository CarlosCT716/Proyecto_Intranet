package com.cibertec.intranet.asistente.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_ia_historial")
@Data
public class HistorialInteraccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInteraccion;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "pregunta_usuario", columnDefinition = "TEXT")
    private String preguntaUsuario;

    @Column(name = "respuesta_ia", columnDefinition = "TEXT")
    private String respuestaIa;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;
    */
}