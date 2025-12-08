package com.cibertec.intranet.matricula.repository;

import com.cibertec.intranet.matricula.model.DetalleMatricula;
import com.cibertec.intranet.profesor.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula,Integer> {
    Nota findByIdDetalle(Integer idDetalle);

}
