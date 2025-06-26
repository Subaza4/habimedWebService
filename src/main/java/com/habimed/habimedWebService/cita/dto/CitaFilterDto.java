package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaFilterDto {
    private Integer idCita;
    private Integer idPaciente;
    private Integer idDoctor;
    private Integer idServicio;
    private Integer idConsultorio;
    private EstadoCitaEnum estado;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
}
