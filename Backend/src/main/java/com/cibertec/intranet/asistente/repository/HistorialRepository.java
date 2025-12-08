package com.cibertec.intranet.asistente.repository;

import com.cibertec.intranet.asistente.model.HistorialInteraccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialRepository extends JpaRepository<HistorialInteraccion, Integer> {
}