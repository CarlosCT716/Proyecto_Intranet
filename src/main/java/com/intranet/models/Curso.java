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
	
	@Column(name = "nombre_curso")
	private String nombreCurso;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_carrera")
	private Carrera carrera;
	
	private Integer ciclo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_profesor")
	private Usuario usuario;
	
	@Column(name = "estado", columnDefinition = "BIT NOT NULL DEFAULT 1")
	private Boolean estado = true;
	
	private Integer creditos;
}
