package com.cibertec.intranet.admin.service;

import com.cibertec.intranet.admin.dto.AdminDashboardDTO;
import com.cibertec.intranet.asistente.repository.HistorialRepository;
import com.cibertec.intranet.auditoria.model.Auditoria;
import com.cibertec.intranet.auditoria.repository.AuditoriaRepository;
import com.cibertec.intranet.matricula.repository.MatriculaRepository;
import com.cibertec.intranet.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;
    private final HistorialRepository historialRepository;
    private final AuditoriaRepository auditoriaRepository;

    @Transactional(readOnly = true)
    public AdminDashboardDTO obtenerDataDashboard() {
        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalEstudiantes(usuarioRepository.countByRol_NombreRol("ROLE_ALUMNO"));
        dto.setDocentesActivos(usuarioRepository.countByRol_NombreRolAndActivoTrue("ROLE_PROFESOR"));
        
        dto.setIncidenciasIA(historialRepository.count());

        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = LocalDate.now().atTime(LocalTime.MAX);
        dto.setAuditoriaHoy(auditoriaRepository.countByFechaBetween(inicioDia, finDia));

        dto.setEstudiantesPorCarrera(matriculaRepository.obtenerEstudiantesPorCarrera());
        dto.setDistribucionUsuarios(usuarioRepository.obtenerDistribucionUsuarios());

        List<Auditoria> ultimos = auditoriaRepository.findTop5ByOrderByFechaDesc();
        
        List<AdminDashboardDTO.AuditoriaResumenDTO> movimientos = ultimos.stream().map(a -> {
            AdminDashboardDTO.AuditoriaResumenDTO mov = new AdminDashboardDTO.AuditoriaResumenDTO();
            
            if (a.getUsuario() != null) {
                mov.setUsuario(a.getUsuario().getUsername());
            } else {
                mov.setUsuario("Sistema");
            }
            
            mov.setAccion(a.getAccion());
            mov.setTabla(a.getTablaAfectada());
            mov.setFecha(a.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            mov.setIp(a.getIpOrigen());
            return mov;
        }).collect(Collectors.toList());

        dto.setUltimosMovimientos(movimientos);

        return dto;
    }
}