package com.habimed.habimedWebService.persona.domain.model;

import java.time.LocalDate;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Persona {
    // dni, nombre, apellidos, correo, celular, direccion, fecha_nacimiento
    private Long dni;
    private String nombres;
    private String apellidos;
    private String celular;
    private String direccion;
    private LocalDate fecha_nacimiento;
}
