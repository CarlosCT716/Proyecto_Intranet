package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.HorarioFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Horario;
import com.intranet.repo.IRepoHorario;

@Service
public class HorarioService {
    @Autowired private IRepoHorario _horario;
    
    @Autowired
	private AuditoriaService auService;


    public List<Horario> getAll() {
        return _horario.findAllByOrderByIdHorarioDesc();
    }

    public List<Horario> search(HorarioFilter filtro) {
        return _horario.findAllWithFilters(filtro.getIdModalidad(), filtro.getIdCarrera());
    }
    public List<Horario> getbyFilters(HorarioFilter filtro){
    	return _horario.findByHorario(filtro.getIdCiclo(),filtro.getIdCarrera());
    }


    public ResultadoResponse create(Horario horario) {
        try {
        	Horario reg = _horario.save(horario);
          auService.log("CREATE", "Horario", reg.getIdHorario().toString(), "Creacion de horario");
            return new ResultadoResponse(true, "Horario registrado con ID " + reg.getIdHorario());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al registrar: " + e.getMessage());
        }
    }

    public Horario getOne(Integer id) {
        return _horario.findById(id).orElseThrow();
    }

    public ResultadoResponse update(Horario horario) {
        try {
        	Horario reg = _horario.save(horario);
        	auService.log("UPDATE", "Horario", reg.getIdHorario().toString(), "Cambios en horario");
            return new ResultadoResponse(true, "Horario ID " + reg.getIdHorario() + " actualizado");
        } catch(Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al actualizar: " + e.getMessage());
        }
    }

    public ResultadoResponse cambiarEstado(Integer id) {
    	Horario h = getOne(id);
        h.setEstado(!h.getEstado());
        try {
        	_horario.save(h);
            String accion = h.getEstado() ? "activado" : "desactivado";
            auService.log("ESTADO", "Horario", h.getIdHorario().toString(), "Estado: " + accion);
            return new ResultadoResponse(true, "Horario ID " + id + " " + accion);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al cambiar estado: " + e.getMessage());
        }
    }
	public List<Horario> getActivos() {
		return _horario.findAllByEstado(true);
	}
}
