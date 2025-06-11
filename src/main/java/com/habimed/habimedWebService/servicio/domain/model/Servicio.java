package com.habimed.habimedWebService.servicio.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor   // Genera un constructor sin argumentos
@AllArgsConstructor  // Genera un constructor con todos los argumentos
public class Servicio {

    private Integer idservicio; // idservicio INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    private Integer idespecialidad; // idespecialidad INT NOT NULL (Clave foránea)

    private String nombre; // nombre VARCHAR(100) NOT NULL

    private String descripcion; // descripcion VARCHAR(500) NULL

    private String riesgos; // riesgos VARCHAR(500) NULL
}