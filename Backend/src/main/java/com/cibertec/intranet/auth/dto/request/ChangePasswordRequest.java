package com.cibertec.intranet.auth.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String usuario;
    private String nuevaContrasena;
}