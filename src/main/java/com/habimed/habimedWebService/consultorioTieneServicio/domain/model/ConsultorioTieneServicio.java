package com.habimed.habimedWebService.consultorioTieneServicio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicio {
    private Integer idCita;
    private Integer idPaciente;
    private Integer idDoctor;
    private String motivo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;
    private String descripcion;
}