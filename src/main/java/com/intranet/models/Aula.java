package com.intranet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_aula")
@Getter
@Setter
public class Aula {
	@Id
	@Column(name="id_aula")
	private Integer idAula;
	@Column(name="descripcion")
	private String descripcion;
}

