package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import lombok.Data;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
public class CitaUpdateDto {
    private String motivo;

    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private EstadoCitaEnum estado;

    private String descripcion;
}
