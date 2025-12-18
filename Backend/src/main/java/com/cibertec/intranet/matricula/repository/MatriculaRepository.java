package com.cibertec.intranet.matricula.repository;

import com.cibertec.intranet.admin.dto.AdminDashboardDTO;
import com.cibertec.intranet.matricula.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula,Integer> {

    Matricula findByAlumno_IdUsuario(Integer idUsuario);
    Optional<Matricula> findTopByAlumnoIdUsuarioOrderByFechaMatriculaDesc(Integer idAlumno);
@Query("SELECT new com.cibertec.intranet.admin.dto.AdminDashboardDTO$ChartDataDTO(m.carrera.nombreCarrera, COUNT(m)) FROM Matricula m GROUP BY m.carrera.nombreCarrera")
    List<AdminDashboardDTO.ChartDataDTO> obtenerEstudiantesPorCarrera();
}