package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Tipo;
import com.intranet.models.Usuario;
import com.intranet.repo.IRepoTipo;
import com.intranet.repo.IRepoUsuario;

@Service
public class AlumnoService {
	@Autowired
	private IRepoUsuario repo;

	@Autowired
	private IRepoTipo repoTipo;

	@Autowired
	private AuditoriaService auService;

	public List<Usuario> getAll() {
		Tipo alumno = repoTipo.findById(2).orElseThrow(); // Tipo 2 = Alumno
		return repo.findByTipo(alumno);
	}

	public Usuario getOne(Integer id) {
		return repo.findById(id).orElseThrow();
	}

	public ResultadoResponse create(Usuario alumno) {
		try {
			alumno.setTipo(repoTipo.findById(2).orElseThrow());
			alumno.setEstado(true);
			Usuario saved = repo.save(alumno);
			auService.log("CREATE", "Alumno", alumno.getIdUsuario().toString(), alumno.getUsuario().toString()) ;
			return new ResultadoResponse(true, "Alumno registrado con ID " + saved.getIdUsuario());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al registrar alumno: " + e.getMessage());
		}
	}

	public ResultadoResponse update(Usuario alumno) {
		try {
			alumno.setTipo(repoTipo.findById(2).orElseThrow());
			Usuario updated = repo.save(alumno);
			auService.log("UPDATE", "Alumno", alumno.getIdUsuario().toString(), alumno.getUsuario().toString()) ;
			return new ResultadoResponse(true, "Alumno ID " + updated.getIdUsuario() + " actualizado");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al actualizar alumno: " + e.getMessage());
		}
	}

	public ResultadoResponse cambiarEstado(Integer id) {
		try {
			Usuario alumno = getOne(id);
			alumno.setEstado(!alumno.getEstado());
			repo.save(alumno);
			String estado = alumno.getEstado() ? "activado" : "desactivado";
			auService.log("ESTADO", "Alumno",alumno.getIdUsuario().toString(), "Estado: " + estado);
			return new ResultadoResponse(true, "Alumno ID " + id + " " + estado);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al cambiar estado del alumno: " + e.getMessage());
		}
	}
}
