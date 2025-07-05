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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	
	@NotBlank(message = "El nombre es requerido")
	@Column(name="nombres")
	private String nombres;
	
	@NotBlank(message = "El apellido es requerido")
	@Column(name="apellidos")
	private String apellidos;
	
	@NotBlank(message = "El usuario es requerido")
	@Column(name="usuario")
	private String usuario;
	
	@NotBlank(message = "El nombre es requerida")
	@Column(name="correo")
	private String correo;
	
	@NotBlank(message = "La contraseña es requerida")
	@Column(name="clave")
	private String password;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tipo")
	private Tipo tipo;
	
	@Column(name = "estado", columnDefinition = "BIT NOT NULL DEFAULT 1")
	private Boolean estado;
	
	@NotNull(message = "Debe asignar una carrera")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="carrera")
	private Carrera carrera;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ciclo")
	private Ciclo ciclo ;
}
