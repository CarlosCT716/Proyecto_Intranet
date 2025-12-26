package com.cibertec.intranet.usuario.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String username;
    private String nombres;
    private String apellidos;
    private String email;
    private String dni;
    private String telefono;
    private String direccion;
    private String rol;
    private Boolean activo;
    private Integer idRol;
}