package com.habimed.habimedWebService.seguridad.domain.model;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class Persona {
    // dni, nombre, apellidos, correo, celular, direccion, fecha_nacimiento
    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 10000000, message = "El DNI debe tener al menos 8 dígitos")
    private int dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "El correo electrónico no es válido")
    private String correo;
    private String celular;
    private String direccion;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fecha_nacimiento;
}
