package com.habimed.habimedWebService.especialidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {
    private Integer idespecialidad; // idespecialidad INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
    private String nombre;          // nombre VARCHAR(45) NOT NULL
    private String descripcion;     // descripcion VARCHAR(255) NULL
}