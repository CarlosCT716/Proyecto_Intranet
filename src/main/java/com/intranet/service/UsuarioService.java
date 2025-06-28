package com.intranet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Tipo;
import com.intranet.models.Usuario;
import com.intranet.repo.IRepoTipo;
import com.intranet.repo.IRepoUsuario;

@Service
public class UsuarioService {
	@Autowired
	IRepoUsuario _usuario;
	@Autowired
	IRepoTipo _tipo;

	public List<Usuario> getAll() {
		return _usuario.findAll();
	}
	
	public List<Usuario> getProfesores() {
		  Tipo profesor = _tipo.findById(1).orElseThrow(); 
		    return _usuario.findByTipo(profesor);
	}
	public List<Usuario> getAlumnos() {
		  Tipo alumnos = _tipo.findById(2).orElseThrow(); 
		    return _usuario.findByTipo(alumnos);
	}


}
