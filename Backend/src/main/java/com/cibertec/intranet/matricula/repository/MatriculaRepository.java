package com.cibertec.intranet.matricula.repository;

import com.cibertec.intranet.matricula.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula,Integer> {

    Matricula findByAlumno_IdUsuario(Integer idUsuario);
    Optional<Matricula> findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(Integer idAlumno);
}
