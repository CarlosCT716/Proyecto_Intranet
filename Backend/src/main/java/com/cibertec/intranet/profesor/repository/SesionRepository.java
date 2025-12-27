package com.cibertec.intranet.profesor.repository;

import com.cibertec.intranet.academico.model.Curso;
import com.cibertec.intranet.profesor.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    List<Sesion> findByCurso_IdCursoOrderByFechaDesc(Integer idCurso);

    List<Sesion> findByCursoProfesorIdUsuarioAndFecha(Integer idProfesor, LocalDate fecha);

    List<Sesion> findByCursoProfesorIdUsuarioAndEstadoSesion(Integer idProfesor, String estadoSesion);
    long countByCurso_IdCurso(Integer idCurso);

    boolean existsByCursoIdCursoAndFecha(Integer idCurso, LocalDate fechaCalculada);

  List<Sesion> findByCurso_IdCursoOrderByFechaAsc(Integer idCurso);
}