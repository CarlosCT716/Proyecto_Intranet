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
public class ProfesorService {

    @Autowired
    private IRepoUsuario repo;

    @Autowired
    private IRepoTipo repoTipo;
    
    @Autowired
    private AuditoriaService auService;

    public List<Usuario> getAll() {
        Tipo profesor = repoTipo.findById(1).orElseThrow(); // Tipo 1 = Profesor
        return repo.findByTipo(profesor);
    }

    public Usuario getOne(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    public ResultadoResponse create(Usuario profesor) {
        try {
            profesor.setTipo(repoTipo.findById(1).orElseThrow());
            profesor.setEstado(true); 
            profesor.setCiclo(null);
            Usuario saved = repo.save(profesor);
            auService.log("CREATE", "Profesor", profesor.getIdUsuario().toString(), profesor.getUsuario().toString()) ;
            return new ResultadoResponse(true, "Profesor registrado con ID " + saved.getIdUsuario());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al registrar profesor: " + e.getMessage());
        }
    }

    public ResultadoResponse update(Usuario profesor) {
    	try {
			profesor.setTipo(repoTipo.findById(1).orElseThrow());
			Usuario updated = repo.save(profesor);
			auService.log("UPDATE", "Profesor", profesor.getIdUsuario().toString(), profesor.getUsuario().toString()) ;
			return new ResultadoResponse(true, "Profesor ID " + updated.getIdUsuario() + " actualizado");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultadoResponse(false, "Error al actualizar profesor: " + e.getMessage());
		}
    }

    public ResultadoResponse cambiarEstado(Integer id) {
        try {
            Usuario profesor = getOne(id);
            profesor.setEstado(!profesor.getEstado());
            repo.save(profesor);
            String estado = profesor.getEstado() ? "activado" : "desactivado";
            auService.log("ESTADO", "Profesor",profesor.getIdUsuario().toString(), "Estado: " + estado);
            return new ResultadoResponse(true, "Profesor ID " + id + " " + estado);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al cambiar estado del profesor: " + e.getMessage());
        }
    }
}

