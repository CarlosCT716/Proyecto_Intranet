package com.cibertec.intranet.auditoria.repository;

import com.cibertec.intranet.auditoria.model.Auditoria;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {
    long countByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<Auditoria> findTop5ByOrderByFechaDesc();
}