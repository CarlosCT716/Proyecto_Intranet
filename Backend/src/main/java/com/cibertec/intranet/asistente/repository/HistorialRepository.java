package com.cibertec.intranet.asistente.repository;

import com.cibertec.intranet.asistente.model.HistorialInteraccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialRepository extends JpaRepository<HistorialInteraccion, Integer> {
    List<HistorialInteraccion> findTop5ByIdUsuarioOrderByFechaDesc(Integer idUsuario);
}