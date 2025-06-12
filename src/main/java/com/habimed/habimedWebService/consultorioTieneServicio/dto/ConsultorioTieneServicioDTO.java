package com.habimed.habimedWebService.consultorioTieneServicio.dto;

import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicioDTO {
    private Integer idCita;
    private Integer idPaciente;
    private Integer idDoctor;
    private String motivo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;
    private String descripcion;

}
