package com.intranet.models;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Curso {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_curso")
	private Integer idCurso;
	
	@NotBlank(message = "El nombre es requerida")
	@Column(name = "nombre_curso", nullable = false)
	private String nombreCurso;
	
	@NotNull(message = "Debe seleccionar una carrera")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_carrera", nullable = false)
	private Carrera carrera;
	
	@NotNull(message = "Debe seleccionar un ciclo")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ciclo", nullable = false)
	private Ciclo ciclo;
	
	@NotNull(message = "Debe asignar un profesor")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_profesor", nullable = false)
	private Usuario usuario;
	
	@Column(name = "estado", columnDefinition = "BIT NOT NULL DEFAULT 1", nullable = false)
	private Boolean estado = true;
	
	@NotNull(message = "Debe ingresar los créditos")
	@Min(value = 1, message = "El mínimo es 1 crédito")
	@Max(value = 10, message = "El máximo es 10 créditos")
	@Column(name = "creditos", nullable = false)
	private Integer creditos;
}
