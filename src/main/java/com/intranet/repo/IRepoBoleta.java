package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.models.Boleta;

public interface IRepoBoleta extends JpaRepository<Boleta, Integer> {
	List<Boleta> findAllByOrderByIdBoletaDesc();
}
