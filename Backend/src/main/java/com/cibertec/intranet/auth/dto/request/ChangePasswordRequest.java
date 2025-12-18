package com.cibertec.intranet.auth.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Integer idUsuario;
    private String nuevaContrasena;
}