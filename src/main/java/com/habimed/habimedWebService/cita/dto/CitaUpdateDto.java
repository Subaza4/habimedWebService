package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaUpdateDto {
    private Integer idcita;

    private Integer idservicio;

    private Integer idconsultorio;

    private Integer idmedico;

    private String dniPersona;

    private String motivo;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private EstadoCitaEnum estado;

    private String descripcion;
}
