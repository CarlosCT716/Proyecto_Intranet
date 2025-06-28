package com.intranet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.models.Carrera;

public interface IRepoCarrera extends JpaRepository<Carrera, Integer> {
	
}
