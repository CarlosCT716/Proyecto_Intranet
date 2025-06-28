package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Modalidad;
import com.intranet.repo.IRepoModalidad;

@Service
public class ModalidadService {
	@Autowired
	IRepoModalidad _modalidad;

	public List<Modalidad> getAll() {
		return _modalidad.findAll();
	}
}

