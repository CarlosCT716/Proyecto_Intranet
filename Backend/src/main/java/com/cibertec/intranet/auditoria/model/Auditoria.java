package com.cibertec.intranet.auditoria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_auditoria")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAuditoria;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false)
    private String accion; // CREATE, UPDATE, DELETE

    @Column(name = "tabla_afectada")
    private String tablaAfectada;

    @Column(name = "detalle_anterior", columnDefinition = "TEXT")
    private String detalleAnterior;

    @Column(name = "detalle_nuevo", columnDefinition = "TEXT")
    private String detalleNuevo;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "ip_origen")
    private String ipOrigen;
}