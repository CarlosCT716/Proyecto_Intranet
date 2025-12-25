package com.cibertec.intranet.auditoria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.cibertec.intranet.usuario.model.Usuario;

@Entity
@Table(name = "tb_auditoria")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAuditoria;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private String accion; 

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