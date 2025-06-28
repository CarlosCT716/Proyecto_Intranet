package com.intranet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_usuarios")
@Getter
@Setter
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;
	
	@Column(name="nombres")
	private String nombres;
	
	@Column(name="apellidos")
	private String apellidos;
	
	@Column(name="usuario")
	private String usuario;
	
	@Column(name="correo")
	private String correo;
	
	@Column(name="clave")
	private String password;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tipo")
	private Tipo tipo;
	
	@Column(name = "estado", columnDefinition = "BIT NOT NULL DEFAULT 1")
	private Boolean estado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="carrera")
	private Carrera carrera;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ciclo")
	private Ciclo ciclo ;
}
