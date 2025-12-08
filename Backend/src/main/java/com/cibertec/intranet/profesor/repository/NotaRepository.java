package com.cibertec.intranet.profesor.repository;

import com.cibertec.intranet.profesor.model.Nota;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota,Integer> {
    List<Nota> findByDetalleMatricula_Curso_IdCurso(Integer idCurso);
    Nota findByDetalleMatricula_IdDetalle(Integer iddetalle);
}
