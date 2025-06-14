package com.habimed.habimedWebService.horarioDoctor.dto;

import com.habimed.habimedWebService.usuario.dto.UsuarioResponseDto;
import lombok.Data;
import com.medic.dto.usuario.UsuarioResponseDto;
import java.time.LocalTime;

@Data
public class HorarioDoctorResponseDto {
    private Integer idHorarioDoctor;
    private Integer idDoctor;
    private UsuarioResponseDto doctor;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionMinutos;
}