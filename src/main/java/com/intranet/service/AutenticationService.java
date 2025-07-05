package com.intranet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.dtos.AutenticationFilter;
import com.intranet.models.Usuario;
import com.intranet.repo.IRepoUsuario;

import jakarta.servlet.http.HttpSession;

@Service
public class AutenticationService {
	@Autowired
	private IRepoUsuario _usuarioRepo;

	public Usuario autenticar(AutenticationFilter filter) {
		return _usuarioRepo.findByUsuarioAndPassword(filter.getUsuario(), filter.getPassword());
	}

	private boolean Restriccion(HttpSession session, int tEsperado) {
		Object tipo = session.getAttribute("tipo");
		return tipo != null && tipo.equals(tEsperado);
	}

	public boolean Admin(HttpSession session) {
		return Restriccion(session, 3);
	}

	public boolean Profesor(HttpSession session) {
		return Restriccion(session, 1);
	}

	public boolean Alumno(HttpSession session) {
		return Restriccion(session, 2);
	}
}
