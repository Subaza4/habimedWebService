package com.habimed.habimedWebService.horarioDoctor.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class HorarioDoctorFilterDto {
    private Integer idhorariodoctor;

    private Integer iddoctor;

    private String diaSemana;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private Integer duracionMinutos;
}
