package com.intranet.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	@Column(name="username")
    private String username;         
	@Column(name="accion")
    private String accion;           
	@Column(name="entidad")
    private String entityName;       
	@Column(name="id_entidad")
    private String entityId;
	@Column(name="descripcion")
    private String descripcion;    
	@Column(name="fecha_registro")
    private LocalDateTime fecha; 

}
