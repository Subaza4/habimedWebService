package com.habimed.habimedWebService.horarioDoctor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDoctorInsertDto {
    @NotNull(message = "El ID del doctor es obligatorio")
    private Integer idDoctor;

    @Size(max = 15, message = "El día de la semana debe tener máximo 15 caracteres")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalDateTime horaInicio;
    //private Time horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalDateTime horaFin;

    private Integer duracionMinutos; // Se calcula automáticamente
}