package com.intranet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intranet.models.Tipo;
import com.intranet.models.Usuario;

public interface IRepoUsuario extends JpaRepository<Usuario, Integer> {
	Usuario findByUsuarioAndPassword(String usuario, String clave);
	
    List<Usuario> findByTipo(Tipo tipo);

}
