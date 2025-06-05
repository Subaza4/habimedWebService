package com.habimed.habimedWebService.horarioDoctor.dto;

import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDoctorResponse {
    private Integer idhorariodoctor; // idhorariodoctor INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    private Integer iddoctor;

    private String nombreDoctor; // Para incluir el nombre del doctor en la respuesta

    private String diaSemana;

    private String nombreDiaSemana; // Para incluir el nombre del día de la semana en la respuesta

    private LocalTime horaInicio; 

    private LocalTime horaFin;

    private Integer duracionMinutos;

    public RowMapper<HorarioDoctorResponse> horarioDoctorRowMapper() {
        return (rs, rowNum) -> {
            HorarioDoctorResponse horario = new HorarioDoctorResponse();
            horario.setIdhorariodoctor(rs.getInt("idhorariodoctor"));
            horario.setIddoctor(rs.getInt("iddoctor"));
            horario.setNombreDoctor(rs.getString("nombredoctor"));
            horario.setDiaSemana(rs.getString("dia_semana"));
            horario.setNombreDiaSemana(rs.getString("nombre_dia_semana"));
            horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
            horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());
            horario.setDuracionMinutos(rs.getInt("duracion_minutos"));
            return horario;
        };
    }
}
