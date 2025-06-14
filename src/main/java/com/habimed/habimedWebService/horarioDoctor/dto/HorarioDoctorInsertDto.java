package com.habimed.habimedWebService.horarioDoctor.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

@Data
public class HorarioDoctorInsertDto {
    @NotNull(message = "El ID del doctor es obligatorio")
    private Integer idDoctor;

    @NotBlank(message = "El día de la semana es obligatorio")
    @Size(max = 15, message = "El día de la semana no puede exceder 15 caracteres")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Integer duracionMinutos;
}