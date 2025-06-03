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

    @Size(min = 8, max = 8, message = "El DNI de la persona debe tener exactamente 8 dígitos.")
    private String dniPersona; // dni_persona VARCHAR(8) NOT NULL (Clave foránea)

    @FutureOrPresent(message = "La fecha y hora de inicio de la cita debe ser en el presente o futuro.")
    private LocalDateTime fechaHoraInicio; // fecha_hora_inicio TIMESTAMP NOT NULL

    @FutureOrPresent(message = "La fecha y hora de fin de la cita debe ser en el presente o futuro.")
    private LocalDateTime fechaHoraFin; // fecha_hora_fin TIMESTAMP NOT NULL

    @Size(max = 20, message = "El estado de la cita debe tener máximo 20 caracteres.")
    private String estado; // estado VARCHAR(20) NOT NULL (Ej: 'Programada', 'Confirmada', 'Cancelada', 'Completada')
}