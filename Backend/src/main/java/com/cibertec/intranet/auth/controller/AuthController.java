package com.cibertec.intranet.auth.controller;

import com.cibertec.intranet.auth.dto.request.ChangePasswordRequest;
import com.cibertec.intranet.auth.dto.request.LoginRequest;
import com.cibertec.intranet.auth.dto.response.LoginResponse;
import com.cibertec.intranet.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/cambiar-contrasena")
    public ResponseEntity<Void> cambiarContrasena(@RequestBody ChangePasswordRequest request) {
        authService.cambiarContrasena(request);
        return ResponseEntity.ok().build();
    }
}