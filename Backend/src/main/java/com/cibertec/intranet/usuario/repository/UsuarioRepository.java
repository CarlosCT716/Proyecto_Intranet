package com.cibertec.intranet.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cibertec.intranet.admin.dto.AdminDashboardDTO;
import com.cibertec.intranet.usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    long countByRol_NombreRol(String nombreRol);

    long countByRol_NombreRolAndActivoTrue(String nombreRol);

    @Query("SELECT new com.cibertec.intranet.admin.dto.AdminDashboardDTO$ChartDataDTO(u.rol.nombreRol, COUNT(u)) FROM Usuario u GROUP BY u.rol.nombreRol")
    List<AdminDashboardDTO.ChartDataDTO> obtenerDistribucionUsuarios();
    List<Usuario> findByActivoTrue();
}