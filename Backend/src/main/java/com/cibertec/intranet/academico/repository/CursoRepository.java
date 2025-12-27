package com.cibertec.intranet.academico.repository;

import com.cibertec.intranet.academico.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByCarreraIdCarreraAndCicloIdCiclo(Integer idCarrera, Integer idCiclo);
    List<Curso> findByProfesor_IdUsuario(Integer idProfesor);
    List<Curso> findByProfesorIdUsuarioAndActivoTrue(Integer idProfesor);
    List<Curso> findByActivoTrue();
}