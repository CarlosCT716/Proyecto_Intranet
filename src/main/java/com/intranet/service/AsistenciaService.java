package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.CursoFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Asistencia;
import com.intranet.repo.IRepoAsistencias;

@Service
public class AsistenciaService {
	@Autowired
	private IRepoAsistencias _asistencias;

	public List<Asistencia> getAll() {
		return _asistencias.findAll();
	}

	public List<Asistencia> getAsistenciasPorCurso(CursoFilter filter) {
		return _asistencias.findAllWithFilters(filter.getIdCurso(), filter.getIdUsuario());
	}

	public Asistencia getbyID(Integer id) {
		return _asistencias.findById(id).orElseThrow();
	}
	
	public List<Asistencia> getAsistenciasByAlumno(CursoFilter filter) {
		return _asistencias.findByAlumno(filter.getIdUsuario());
	}


	public ResultadoResponse guardar(Asistencia asistencia) {
		try {
			Asistencia reg = _asistencias.save(asistencia);
			return new ResultadoResponse(true, "Asistencia actualizada, id:  " + reg.getIdAsistencia());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al actualizar: " + e.getMessage());
		}
	}

}
