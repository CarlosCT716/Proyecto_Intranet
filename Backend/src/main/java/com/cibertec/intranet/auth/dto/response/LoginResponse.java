package com.cibertec.intranet.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Integer idUsuario;
    private String username;
    private String nombres;
    private String apellidos;
    private String rol;
}