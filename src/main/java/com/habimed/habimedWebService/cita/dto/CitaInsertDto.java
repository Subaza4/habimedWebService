package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaInsertDto {
    @NotNull(message = "El ID del paciente es obligatorio")
    private Integer idPaciente;

    @NotNull(message = "El ID del doctor es obligatorio")
    private Integer idDoctor;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Integer idServicio;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha y hora de fin es obligatoria")
    private LocalDateTime fechaHoraFin;

    private String descripcion;
}
