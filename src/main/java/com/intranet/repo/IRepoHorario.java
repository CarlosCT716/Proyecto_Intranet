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
	         where (:aula is null or h.aula.idAula = :aula)
	           and (:modalidad is null or h.modalidad.idModalidad = :modalidad)
	        order by h.idHorario desc
	    """)
	    List<Horario> findAllWithFilters(@Param("aula") Integer aula,
	                                   @Param("modalidad") Integer modalidad);
}