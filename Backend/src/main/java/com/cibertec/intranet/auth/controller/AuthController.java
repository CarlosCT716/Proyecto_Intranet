package com.cibertec.intranet.auth.controller;

import com.cibertec.intranet.auth.dto.response.LoginResponse; // <--- Cambio aquí
import com.cibertec.intranet.auth.dto.request.LoginRequest;
import com.cibertec.intranet.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService _auth;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(_auth.login(request));
    }
}