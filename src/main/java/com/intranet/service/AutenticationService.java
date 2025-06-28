package com.intranet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.AutenticationFilter;
import com.intranet.models.Usuario;
import com.intranet.repo.IRepoUsuario;

@Service
public class AutenticationService {
	@Autowired
	private IRepoUsuario _usuarioRepo;
	
	public Usuario autenticar(AutenticationFilter filter) {
		return  _usuarioRepo.findByUsuarioAndPassword(filter.getUsuario(), filter.getPassword());
	}
}
