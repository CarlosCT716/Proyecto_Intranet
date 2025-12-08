package com.cibertec.intranet.profesor.repository;

import com.cibertec.intranet.profesor.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    Optional<Asistencia> findBySesion_IdSesionAndAlumno_IdUsuario(Integer idSesion, Integer idAlumno);

    List<Asistencia> findBySesion_IdSesion(Integer idSesion);

    int countByAlumno_IdUsuarioAndSesion_Curso_IdCurso(Integer idAlumno, Integer idCurso);

    int countByAlumno_IdUsuarioAndSesion_Curso_IdCursoAndIdEstado(Integer idAlumno, Integer idCurso, Integer idEstado
    );


}
