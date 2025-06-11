package com.habimed.habimedWebService.persona.domain.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Persona {
    // dni, nombre, apellidos, correo, celular, direccion, fecha_nacimiento
    private Long dni;
    private String nombre;
    private String apellidos;
    private String correo;
    private String celular;
    private String direccion;
    private Date fecha_nacimiento;
}
