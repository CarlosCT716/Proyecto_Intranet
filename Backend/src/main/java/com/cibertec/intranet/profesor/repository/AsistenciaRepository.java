package com.cibertec.intranet.profesor.repository;

import com.cibertec.intranet.profesor.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    Optional<Asistencia> findBySesion_IdSesionAndAlumno_IdUsuario(Integer idSesion, Integer idAlumno);

    List<Asistencia> findBySesion_IdSesion(Integer idSesion);

    int countByAlumno_IdUsuarioAndSesion_Curso_IdCurso(Integer idAlumno, Integer idCurso);

    int countByAlumno_IdUsuarioAndSesion_Curso_IdCursoAndIdEstado(Integer idAlumno, Integer idCurso, Integer idEstado
    );

    @Query("SELECT a FROM Asistencia a WHERE a.alumno.idUsuario = :idAlumno AND a.sesion.curso.idCurso = :idCurso ORDER BY a.sesion.fecha ASC")
    List<Asistencia> listarPorAlumnoYCurso(@Param("idAlumno") Integer idAlumno, @Param("idCurso") Integer idCurso);
}
