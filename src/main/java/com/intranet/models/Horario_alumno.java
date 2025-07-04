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
@Table(name = "tb_horarios_alumno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Horario_alumno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_horario_alumno")
	private Integer idHorarioA;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_alumno")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_horario")
	private Horario horario;

	public Horario_alumno(Usuario usuario, Horario horario) {
		this.usuario = usuario;
		this.horario = horario;
	}
}
