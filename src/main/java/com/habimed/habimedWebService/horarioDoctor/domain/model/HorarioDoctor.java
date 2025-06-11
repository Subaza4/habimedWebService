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

    private Integer iddoctor; // iddoctor INT NOT NULL (Clave foránea)

    private String diaSemana; // dia_semana VARCHAR(15) NOT NULL

    private LocalTime horaInicio; // hora_inicio TIME NOT NULL

    private LocalTime horaFin; // hora_fin TIME NOT NULL

    private Integer duracionMinutos; // duracion_minutos INT NULL (aunque en tu DDL es NULL, se hace NOT NULL aquí para sentido de negocio)
}