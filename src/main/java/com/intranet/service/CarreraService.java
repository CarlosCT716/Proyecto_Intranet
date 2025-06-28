package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Carrera;
import com.intranet.repo.IRepoCarrera;

@Service
public class CarreraService {
	@Autowired
	IRepoCarrera _carrera;

	public List<Carrera> getAll() {
		return _carrera.findAll();
	}
}
