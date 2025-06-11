package com.habimed.habimedWebService.cita.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CitaResponseDto {

    private Integer idcita; // idcita INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    private Integer idservicio;

    private Integer idconsultorio;

    private Integer idmedico;

    //@Size(min = 8, max = 8, message = "El DNI de la persona debe tener exactamente 8 dígitos.")
    private String dniPersona;

    //@Size(max = 255, message = "El motivo de la cita debe ser descrito en menos de 255 caracteres")
    private String motivo; // dni_persona VARCHAR(8) NOT NULL (Clave foránea)

    //@FutureOrPresent(message = "La fecha y hora de inicio de la cita debe ser en el presente o futuro.")
    private LocalDateTime fechaHoraInicio; // fecha_hora_inicio TIMESTAMP NOT NULL

    //@FutureOrPresent(message = "La fecha y hora de fin de la cita debe ser en el presente o futuro.")
    private LocalDateTime fechaHoraFin; // fecha_hora_fin TIMESTAMP NOT NULL

    //@Size(max = 20, message = "El estado de la cita debe tener máximo 20 caracteres.")
    private String estado; // estado VARCHAR(20) NOT NULL (Ej: 'Programada', 'Confirmada', 'Cancelada', 'Completada')

    private String descripcion;
}
