package com.habimed.habimedWebService.cita.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Importaciones para las anotaciones de validación


@Data                // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor   // Genera un constructor sin argumentos
@AllArgsConstructor  // Genera un constructor con todos los argumentos
public class Cita {

    private Integer idcita; // idcita INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    private Integer iddoctor; // iddoctor INT NOT NULL (Clave foránea)

    private String motivo; // idconsultorio INT NOT NULL (Clave foránea)

    private String descripcion; // idmedico INT NOT NULL (Clave foránea)

    private Integer idpaciente; // id del usuario INT NOT NULL (Clave foránea)

    private LocalDateTime fecha_hora_inicio; // fecha_hora_inicio TIMESTAMP NOT NULL

    private LocalDateTime fecha_hora_fin; // fecha_hora_fin TIMESTAMP NOT NULL

    private String estado; // estado VARCHAR(20) NOT NULL (Ej: 'Programada', 'Confirmada', 'Cancelada', 'Completada')
}