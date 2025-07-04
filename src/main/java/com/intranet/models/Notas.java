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
@Table(name = "tb_notas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notas {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_nota")
	private Integer idNota;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_alumno")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_curso")
	private Curso curso;

	@Column(name = "nota1")
	private Double nota1;

	@Column(name = "nota2")
	private Double nota2;

	@Column(name = "nota3")
	private Double nota3;

	@Column(name = "promedio", insertable = false, updatable = false)
	private Double promedio;

	public Notas(Usuario usuario, Curso curso) {
		this.usuario = usuario;
		this.curso = curso;
		this.nota1 = 0.0;
		this.nota2 = 0.0;
		this.nota3 = 0.0;
	}

}
