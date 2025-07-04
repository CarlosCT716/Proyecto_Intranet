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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_asistencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_asistencia")
	private Integer idAsistencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_alumno")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_curso")
	private Curso curso;

	@Column(name = "inasistencias")
	private Integer inasistencias;

	public Asistencia(Usuario usuario, Curso curso) {
		this.usuario = usuario;
		this.curso = curso;
		this.inasistencias = 0;
	}

}
