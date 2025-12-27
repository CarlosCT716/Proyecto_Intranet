package com.cibertec.intranet.auth.service;

import com.cibertec.intranet.auth.dto.request.ChangePasswordRequest;
import com.cibertec.intranet.auth.dto.request.LoginRequest;
import com.cibertec.intranet.auth.dto.response.LoginResponse;
import com.cibertec.intranet.auth.jwt.jwtUtil;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository _usuario;
    private final jwtUtil jwtUtil;
    private final AuthenticationManager _auth;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        _auth.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Usuario usuario = _usuario.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(
                usuario.getUsername(),
                usuario.getRol().getNombreRol(),
                usuario.getIdUsuario()
        );

        return new LoginResponse(
                token,
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getRol().getNombreRol()
        );
    }

    public void cambiarContrasena(ChangePasswordRequest request) {
        Usuario usuario = _usuario.findByUsername(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(request.getNuevaContrasena()));
        _usuario.save(usuario);
    }
}