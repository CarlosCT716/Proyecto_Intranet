package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.CursoFilter;
import com.intranet.dtos.ResultadoResponse;
import com.intranet.models.Curso;
import com.intranet.repo.IRepoCurso;

@Service
public class CursoService {
    @Autowired private IRepoCurso repo;
    @Autowired private AuditoriaService auService;

    public List<Curso> getAll() {
        return repo.findAllByOrderByIdCursoDesc();
    }

    public List<Curso> search(CursoFilter filtro) {
        return repo.findAllWithFilters(filtro.getIdCarrera(), filtro.getIdUsuario());
    }

    public ResultadoResponse create(Curso curso) {
        try {
            Curso reg = repo.save(curso);
            auService.log("CREATE", "Curso", reg.getIdCurso().toString(), reg.getNombreCurso().toString());
            return new ResultadoResponse(true, "Curso registrado con ID " + reg.getIdCurso());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al registrar: " + e.getMessage());
        }
    }

    public Curso getOne(Integer id) {
        return repo.findById(id).orElseThrow();
    }

    public ResultadoResponse update(Curso curso) {
        try {
            Curso reg = repo.save(curso);
            auService.log("UPDATE", "Curso", reg.getIdCurso().toString(), reg.getNombreCurso().toString());
            return new ResultadoResponse(true, "Curso ID " + reg.getIdCurso() + " actualizado");
        } catch(Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al actualizar: " + e.getMessage());
        }
    }

    public ResultadoResponse cambiarEstado(Integer id) {
        Curso c = getOne(id);
        c.setEstado(!c.getEstado());
        try {
            repo.save(c);
            String accion = c.getEstado() ? "activado" : "desactivado";
           auService.log("DESACTIVADO", "Curso", c.getIdCurso().toString(), "Estado: " + accion);
            return new ResultadoResponse(true, "Curso ID " + id + " " + accion);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResultadoResponse(false, "Error al cambiar estado: " + e.getMessage());
        }
    }
}

