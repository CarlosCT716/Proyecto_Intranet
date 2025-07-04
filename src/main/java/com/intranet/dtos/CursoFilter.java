package com.intranet.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoFilter {
	private Integer idCarrera;
	private Integer idCiclo;
	private Integer idCurso;
	private Integer idUsuario;
}