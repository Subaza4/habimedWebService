package com.habimed.habimedWebService.horarioDoctor.dto;

import java.sql.Time;
import java.time.LocalTime;

import com.habimed.parameterREST.RequestREST;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDoctorRequest extends RequestREST {
    private Integer idhorariodoctor; // idhorariodoctor INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    @NotNull(message = "El ID del doctor no puede ser nulo.")
    private Integer iddoctor;

    @Size(max = 15, message = "El día de la semana debe tener máximo 15 caracteres.")
    private String diaSemana;

    private Time horaInicio;

    private Time horaFin;

    private Integer duracionMinutos;

    public String getConditions() {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idhorariodoctor != null) {
            conditions.append(" AND idhorariodoctor = ").append(idhorariodoctor);
        }
        if (iddoctor != null) {
            conditions.append(" AND iddoctor = ").append(iddoctor);
        }
        if (diaSemana != null && !diaSemana.isEmpty()) {
            conditions.append(" AND dia_semana LIKE '%").append(diaSemana).append("%' ");
        }
        return conditions.toString();
    }
}
