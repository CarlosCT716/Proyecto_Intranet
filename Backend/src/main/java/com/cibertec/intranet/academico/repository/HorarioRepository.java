package com.cibertec.intranet.academico.repository;

import com.cibertec.intranet.academico.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    List<Horario> findByCursoIdCurso(Integer idCurso);
    List<Horario> findByCursoIdCursoIn(List<Integer> cursosIds);
    Horario findFirstByCursoIdCurso(Integer idCurso);
    List<Horario> findByCursoProfesorIdUsuarioAndDiaSemana(Integer idProfesor, String diaSemana);
    List<Horario> findByActivoTrue();
}