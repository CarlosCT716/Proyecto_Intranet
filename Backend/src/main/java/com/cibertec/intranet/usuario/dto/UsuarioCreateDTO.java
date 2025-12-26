package com.cibertec.intranet.usuario.dto;

import lombok.Data;

@Data
public class UsuarioCreateDTO {
    private String username;
    private String password;
    private String nombres;
    private String apellidos;
    private String email;
    private String dni;
    private String telefono;
    private String direccion;
    private Integer idRol;
}