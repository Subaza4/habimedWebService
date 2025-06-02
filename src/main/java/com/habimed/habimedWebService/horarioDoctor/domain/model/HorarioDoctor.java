package com.habimed.habimedWebService.horarioDoctor.domain.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// Importaciones para las anotaciones de validación
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime; // Para el tipo de dato TIME en Java 8+

@Data                // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor   // Genera un constructor sin argumentos
@AllArgsConstructor  // Genera un constructor con todos los argumentos
public class HorarioDoctor {

    private Integer idhorariodoctor; // idhorariodoctor INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    @NotNull(message = "El ID del doctor no puede ser nulo.")
    private Integer iddoctor; // iddoctor INT NOT NULL (Clave foránea)

    @NotBlank(message = "El día de la semana no puede estar vacío.")
    @Size(max = 15, message = "El día de la semana debe tener máximo 15 caracteres.")
    // Podrías añadir un @Pattern si quieres restringir a días específicos (ej. "Lunes|Martes|...")
    private String diaSemana; // dia_semana VARCHAR(15) NOT NULL

    @NotNull(message = "La hora de inicio no puede ser nula.")
    private LocalTime horaInicio; // hora_inicio TIME NOT NULL

    @NotNull(message = "La hora de fin no puede ser nula.")
    private LocalTime horaFin; // hora_fin TIME NOT NULL

    @NotNull(message = "La duración en minutos no puede ser nula.")
    // Puedes añadir @Min(1) si la duración debe ser al menos 1 minuto
    private Integer duracionMinutos; // duracion_minutos INT NULL (aunque en tu DDL es NULL, se hace NOT NULL aquí para sentido de negocio)
                                     // Si en la DB es NULL, puedes quitar @NotNull.
}