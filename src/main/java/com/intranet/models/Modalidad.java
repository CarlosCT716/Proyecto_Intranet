package com.intranet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_modalidad")
@Getter
@Setter
public class Modalidad {
	@Id
	@Column(name="id_modalidad")
	private Integer idModalidad;
	@Column(name="descripcion")
	private String descripcion;
}

