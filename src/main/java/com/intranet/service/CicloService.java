package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Ciclo;
import com.intranet.repo.IRepoCiclo;

@Service
public class CicloService {
	@Autowired
	IRepoCiclo _ciclo;

	public List<Ciclo> getAll() {
		return _ciclo.findAll();
	}
}
