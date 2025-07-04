package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intranet.models.Notas;

public interface IRepoNotas extends JpaRepository<Notas, Integer> {
	@Query("""
		    select n from Notas n
		    join n.usuario u
		    join u.carrera cr
		    where u.idUsuario = :idUsuario
		    and cr.idCarrera = :idCarrera
		""")
		List<Notas> findAllWithFilters(@Param("idUsuario") Integer idUsuario, @Param("idCarrera") Integer idCarrera);

	@Query("""
			select n from Notas n
			join n.curso c
			join c.usuario u
			where ( c.idCurso = :idCurso)
			and ( u.idUsuario = :idUsuario)
			""")
	List<Notas> findByCurso(@Param("idCurso") Integer idCurso, @Param("idUsuario") Integer idUsuario);

}
