package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Aula;
import com.intranet.repo.IRepoAula;

@Service
public class AulaService {
	@Autowired
	IRepoAula _aula;

	public List<Aula> getAll() {
		return _aula.findAll();
	}
}
