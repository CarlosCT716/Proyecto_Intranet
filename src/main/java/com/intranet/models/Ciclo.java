package com.intranet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_ciclos")
@Getter
@Setter
public class Ciclo {
	@Id
	@Column(name="id_ciclo")
	private Integer idCiclo;
	@Column(name="descripcion")
	private String descripcion;
}
