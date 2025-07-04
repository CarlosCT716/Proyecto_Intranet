package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.models.Asistencia;

public interface IRepoAsistencias extends JpaRepository<Asistencia, Integer> {
	List<Asistencia> findByCurso_IdCurso(Integer idCurso);

	@Query("""
			select a from Asistencia a
			join a.curso c
			join c.usuario u
			where ( c.idCurso = :idCurso)
			and ( u.idUsuario = :idUsuario)
			""")
	List<Asistencia> findAllWithFilters(@Param("idCurso") Integer idCurso, @Param("idUsuario") Integer idUsuario);

	@Query("""
				select a from Asistencia a
				join  a.curso c
				join  c.usuario u
				join  a.usuario alu
				where a.usuario.idUsuario = :idUsuario
			""")
	List<Asistencia> findByAlumno(@Param("idUsuario") Integer idUsuario);

}
