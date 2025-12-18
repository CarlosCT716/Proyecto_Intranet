package com.cibertec.intranet.profesor.repository;

import com.cibertec.intranet.profesor.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    List<Sesion> findByCurso_IdCursoOrderByFechaDesc(Integer idCurso);

    List<Sesion> findByCursoProfesorIdUsuarioAndFecha(Integer idProfesor, LocalDate fecha);

    List<Sesion> findByCursoProfesorIdUsuarioAndEstadoSesion(Integer idProfesor, String estadoSesion);
}