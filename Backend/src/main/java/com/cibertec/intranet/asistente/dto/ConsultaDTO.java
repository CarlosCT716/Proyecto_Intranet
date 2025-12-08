package com.cibertec.intranet.asistente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDTO {
    private String mensaje;
    private Integer userId;
}
