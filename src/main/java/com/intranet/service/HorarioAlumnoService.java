package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Horario_alumno;
import com.intranet.repo.IRepoHorarioAlumno;

@Service
public class HorarioAlumnoService {
    
    @Autowired
    private IRepoHorarioAlumno hAlumno;

    public List<Horario_alumno> getHorarioByAlumno(Integer idUsuario) {
        return hAlumno.findByAlumno(idUsuario);
    }
}
