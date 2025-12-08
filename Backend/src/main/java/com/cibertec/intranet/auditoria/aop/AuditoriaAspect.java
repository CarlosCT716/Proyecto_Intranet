package com.cibertec.intranet.auditoria.aop;


import com.cibertec.intranet.auditoria.annotation.Auditable;
import com.cibertec.intranet.auditoria.service.AuditoriaService;
import com.cibertec.intranet.usuario.repository.UsuarioRepository; // NECESARIO
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;
    private final UsuarioRepository usuarioRepository;

    @Around("@annotation(auditable)")
    public Object registrarAuditoria(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

        Integer idUsuario = obtenerIdUsuarioActual();
        String ipOrigen = obtenerIpOrigen();

        Object resultado = joinPoint.proceed();

        Object detalleAnterior = null;
        Object detalleNuevo = resultado;

        auditoriaService.registrarAccion(
                auditable.accion(),
                auditable.tabla(),
                detalleNuevo,
                detalleAnterior,
                idUsuario,
                ipOrigen
        );

        return resultado;
    }

    private Integer obtenerIdUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            return usuarioRepository.findByUsername(username)
                    .map(usuario -> usuario.getIdUsuario())
                    .orElse(null);
        }
        return null;
    }

    private String obtenerIpOrigen() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return "N/A";
    }
}