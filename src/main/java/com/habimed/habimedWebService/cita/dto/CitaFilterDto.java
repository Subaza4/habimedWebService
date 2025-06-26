package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaFilterDto {
    private Integer idCita;

    private Integer idServicio;

    private Integer idConsultorio;

    private Integer idMedico;

    private Long dniPersona;

    private String motivo;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private EstadoCitaEnum estado;

    private String descripcion;
}
