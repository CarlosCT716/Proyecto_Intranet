package com.cibertec.intranet.matricula.dto;

import lombok.Data;
import java.util.List;

@Data
public class ValidacionMatriculaDTO {
    private boolean aptoParaMatricula;
    private String mensaje;
    private List<String> motivosRechazo;
    private Integer siguienteCicloSugericdo;
}