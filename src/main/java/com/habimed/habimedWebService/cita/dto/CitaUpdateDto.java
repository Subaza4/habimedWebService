package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CitaUpdateDto {
    @Size(max = 50, message = "El motivo de la cita debe ser descrito en menos de 50 caracteres")
    private String motivo; // dni_persona VARCHAR(8) NOT NULL (Clave foránea)

    @FutureOrPresent(message = "La fecha y hora de inicio de la cita debe ser en el presente o futuro.")
    private LocalDateTime fechaHoraInicio; // fecha_hora_inicio TIMESTAMP NOT NULL

    @FutureOrPresent(message = "La fecha y hora de fin de la cita debe ser en el presente o futuro.")
    private LocalDateTime fechaHoraFin; // fecha_hora_fin TIMESTAMP NOT NULL

    private EstadoCitaEnum estado; // estado VARCHAR(20) NOT NULL (Ej: 'Programada', 'Confirmada', 'Cancelada', 'Completada')

    private String descripcion;
}
