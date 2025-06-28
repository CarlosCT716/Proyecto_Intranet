package com.intranet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_carrera")
@Getter
@Setter
public class Carrera {
	@Id
	@Column(name="id_carrera")
	private Integer idCarrera;
	@Column(name="descripcion")
	private String descripcion;
}
