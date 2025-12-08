package com.cibertec.intranet.profesor.dto;
import lombok.Data;
import java.util.List;

@Data
public class AsistenciaRequestDTO {
    private Integer idSesion;
    private List<AsistenciaDTO> asistencias;
}
