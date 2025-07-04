package com.intranet.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HorarioSeleccionado {
	private Integer idHorario;
	private String nombreCurso;
	private Integer creditos;
	private String dia;
	private String inicio;
	private String fin;
	private String aula;

	public double getSubtotal() {
		return creditos * 100.0;
	}
}