package com.intranet.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroForm {
	private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String correo;
    private String password;
    private Integer idCarrera;
    private Integer idCiclo;
}
