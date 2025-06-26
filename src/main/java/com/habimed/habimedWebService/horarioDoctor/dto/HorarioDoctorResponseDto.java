package com.habimed.habimedWebService.horarioDoctor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Time;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDoctorResponseDto {
    private Integer idHorarioDoctor;
    private Integer idDoctor;
    private String nombreDoctor;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionMinutos;
}