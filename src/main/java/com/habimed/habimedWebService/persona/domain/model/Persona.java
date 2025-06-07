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
    @NotNull(message = "El DNI es obligatorio")
    @Min(value = 10000000, message = "El DNI debe tener al menos 8 dígitos")
    private Long dni;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;
    //@NotNull
    //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "El correo electrónico no es válido")
    private String correo;
    private String celular;
    private String direccion;
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha_nacimiento;
}
