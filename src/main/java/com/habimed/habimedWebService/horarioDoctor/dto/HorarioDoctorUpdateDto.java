package com.habimed.habimedWebService.horarioDoctor.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

@Data
public class HorarioDoctorUpdateDto {
    @Size(max = 15, message = "El día de la semana no puede exceder 15 caracteres")
    private String diaSemana;

    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionMinutos;
}