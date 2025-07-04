package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.models.Horario_alumno;

public interface IRepoHorarioAlumno extends JpaRepository<Horario_alumno, Integer> {

	@Query("""
				select ha from Horario_alumno ha
				join  ha.horario h
				join  ha.usuario u
				join  h.curso c
				join  c.usuario p
				where u.idUsuario = :idUsuario
			""")
	List<Horario_alumno> findByAlumno(@Param("idUsuario") Integer idUsuario);
}
