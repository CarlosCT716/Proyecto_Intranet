package com.cibertec.intranet.usuario.service;

import com.cibertec.intranet.auditoria.annotation.Auditable;
import com.cibertec.intranet.usuario.dto.UsuarioCreateDTO;
import com.cibertec.intranet.usuario.dto.UsuarioDTO;
import com.cibertec.intranet.usuario.model.Rol;
import com.cibertec.intranet.usuario.model.Usuario;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

     public List<Usuario> listarUsuariosActivos() { return usuarioRepository.findByActivoTrue(); }
    public UsuarioDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirADTO(usuario);
    }

    @Auditable(accion = "CREACION", tabla = "tb_usuario")
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioCreateDTO dto) {
        if(usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Usuario nuevo = new Usuario();
        nuevo.setUsername(dto.getUsername());
        nuevo.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevo.setNombres(dto.getNombres());
        nuevo.setApellidos(dto.getApellidos());
        nuevo.setEmail(dto.getEmail());
        nuevo.setDni(dto.getDni());
        nuevo.setTelefono(dto.getTelefono());
        nuevo.setDireccion(dto.getDireccion());
        nuevo.setFechaRegistro(LocalDateTime.now());
        nuevo.setActivo(true);

        Rol rol = new Rol();
        rol.setIdRol(dto.getIdRol());
        nuevo.setRol(rol);

        Usuario guardado = usuarioRepository.save(nuevo);
        return convertirADTO(guardado);
    }

    @Auditable(accion = "ACTUALIZACIÓN", tabla = "tb_usuario")
    @Transactional
    public UsuarioDTO actualizarUsuario(Integer id, UsuarioCreateDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setNombres(dto.getNombres());
        existente.setApellidos(dto.getApellidos());
        existente.setEmail(dto.getEmail());
        existente.setDni(dto.getDni());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());

        Usuario actualizado = usuarioRepository.save(existente);
        return convertirADTO(actualizado);
    }
    @Auditable(accion = "ESTADO", tabla = "tb_usuario")
    @Transactional
    public void cambiarEstado(Integer id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setActivo(!u.getActivo());
        usuarioRepository.save(u);
    }

    private UsuarioDTO convertirADTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setUsername(u.getUsername());
        dto.setNombres(u.getNombres());
        dto.setApellidos(u.getApellidos());
        dto.setEmail(u.getEmail());
        dto.setDni(u.getDni());
        dto.setTelefono(u.getTelefono());
        dto.setDireccion(u.getDireccion());
        dto.setActivo(u.getActivo());

        if (u.getRol() != null) {
            dto.setRol(u.getRol().getNombreRol());
            dto.setIdRol(u.getRol().getIdRol());
        }
        return dto;
    }
}