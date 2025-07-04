package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.models.Curso;

public interface IRepoCurso extends JpaRepository<Curso, Integer> {
	List<Curso> findAllByOrderByIdCursoDesc();

	@Query("""
			    select c from Curso c
			     where (:idCarrera is null or c.carrera.idCarrera = :idCarrera)
				   and (:idCiclo is null or c.ciclo.idCiclo = :idCiclo)

			    order by c.idCurso desc
			""")
	List<Curso> findAllWithFilters(@Param("idCarrera") Integer idCarrera, 
									@Param("idCiclo") Integer idCiclo);
}
