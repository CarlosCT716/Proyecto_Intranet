package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.CursoFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Notas;
import com.intranet.repo.IRepoNotas;

@Service
public class NotasService {
	@Autowired
	private IRepoNotas notasService;

	public List<Notas> getAll() {
		return notasService.findAll();
	}

	public List<Notas> getByAlumno(CursoFilter filtro) {
		return notasService.findAllWithFilters(filtro.getIdUsuario(), filtro.getIdCarrera());
	}

	public List<Notas> getNotasPorCurso(CursoFilter filter) {
		return notasService.findByCurso(filter.getIdCurso(), filter.getIdUsuario());
	}

	public Notas getbyID(Integer id) {
		return notasService.findById(id).orElseThrow();
	}

	public ResultadoResponse guardar(Notas notas) {
		try {
			Notas reg = notasService.save(notas);
			return new ResultadoResponse(true, "Asistencia actualizada, id:  " + reg.getIdNota());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al actualizar: " + e.getMessage());
		}
	}
}
