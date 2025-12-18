package com.cibertec.intranet.matricula.repository;

import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.profesor.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula,Integer> {
    Nota findByIdDetalle(Integer idDetalle);
    @Query("SELECT d FROM DetalleMatricula d WHERE d.matricula.alumno.idUsuario = :idAlumno AND d.curso.idCurso = :idCurso")
    Optional<DetalleMatricula> findByAlumnoAndCurso(@Param("idAlumno") Integer idAlumno, @Param("idCurso") Integer idCurso);
    Optional<DetalleMatricula> findByMatricula_Alumno_IdUsuarioAndCurso_IdCurso(Integer idAlumno, Integer idCurso);
    List<DetalleMatricula> findByCurso_IdCurso(Integer idCurso);
}
