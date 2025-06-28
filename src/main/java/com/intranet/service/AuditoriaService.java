package com.intranet.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intranet.models.Auditoria;
import com.intranet.repo.IRepoAuditoria;

import jakarta.servlet.http.HttpSession;

@Service
public class AuditoriaService {
	@Autowired
	private IRepoAuditoria _auditoria;

	@Autowired
	private HttpSession session;

	public void log(String action, String entityName, String entityId, String descripcion) {
		Auditoria a = new Auditoria();
		a.setAccion(action);
		a.setEntityName(entityName);
		a.setEntityId(entityId);
		a.setFecha(LocalDateTime.now());
		String username = Optional.ofNullable(session.getAttribute("cuenta")).map(Object::toString).orElse("anónimo");
		a.setUsername(username);

		a.setDescripcion(descripcion);
		_auditoria.save(a);

	}

}
