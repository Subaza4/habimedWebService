package com.habimed.habimedWebService.cita.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Importaciones para las anotaciones de validación
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent; // Para fechas y horas en el futuro o presente


@Data                // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor   // Genera un constructor sin argumentos
@AllArgsConstructor  // Genera un constructor con todos los argumentos
public class Cita {

    private Integer idcita; // idcita INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    private Integer idservicio; // idservicio INT NOT NULL (Clave foránea)

    private Integer idconsultorio; // idconsultorio INT NOT NULL (Clave foránea)

    private Integer idmedico; // idmedico INT NOT NULL (Clave foránea)

    private String dniPersona; // dni_persona VARCHAR(8) NOT NULL (Clave foránea)

    private LocalDateTime fechaHoraInicio; // fecha_hora_inicio TIMESTAMP NOT NULL

    private LocalDateTime fechaHoraFin; // fecha_hora_fin TIMESTAMP NOT NULL

    private String estado; // estado VARCHAR(20) NOT NULL (Ej: 'Programada', 'Confirmada', 'Cancelada', 'Completada')
}