package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.models.Horario;

public interface IRepoHorario extends JpaRepository<Horario, Integer> {
	List<Horario> findAllByOrderByIdHorarioDesc();

	@Query("""
			  select h from Horario h
			  join h.curso c
			  join c.carrera cr
			  where (:modalidad is null or h.modalidad.idModalidad = :modalidad)
			  and (:carrera is null or cr.idCarrera = :carrera)
			  order by h.idHorario desc
			""")
	List<Horario> findAllWithFilters(@Param("modalidad") Integer modalidad, @Param("carrera") Integer carrera);

	@Query("""
			  select h from Horario h
			  join h.curso c
			  join c.ciclo ci
			  join c.carrera cr
			  where ( ci.idCiclo = :ciclo)
			  and ( cr.idCarrera = :carrera)
			  order by h.Dia asc
			""")
	List<Horario> findByHorario(@Param("ciclo") Integer ciclo, @Param("carrera") Integer carrera);

	@Query("""
			  select h from Horario h
			  where ( h.idHorario = :horario)
			  order by h.Dia asc
			""")
	List<Horario> findCursosByHorarioId(@Param("horario") Integer idHorario);

	List<Horario> findAllByEstado(Boolean Estado);
}