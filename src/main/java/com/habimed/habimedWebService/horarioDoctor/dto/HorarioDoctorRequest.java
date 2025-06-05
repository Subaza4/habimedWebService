package com.habimed.habimedWebService.horarioDoctor.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDoctorRequest {
    private Integer idhorariodoctor; // idhorariodoctor INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    @NotNull(message = "El ID del doctor no puede ser nulo.")
    private Integer iddoctor;

    @Size(max = 15, message = "El día de la semana debe tener máximo 15 caracteres.")
    private String diaSemana;

    private LocalTime horaInicio; 

    private LocalTime horaFin;

    private Integer duracionMinutos;
}
