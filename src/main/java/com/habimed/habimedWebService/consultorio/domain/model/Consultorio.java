package com.habimed.habimedWebService.consultorio.domain.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultorio {

    private Integer idconsultorio;

    @NotBlank(message = "El RUC no puede estar vacío")
    @Size(min = 11, max = 11, message = "El RUC debe tener exactamente 11 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El RUC solo debe contener números")
    private String ruc;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 45, message = "El nombre no puede exceder los 45 caracteres")
    private String nombre;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Size(max = 45, message = "La ubicación no puede exceder los 45 caracteres")
    private String ubicacion;

    @Size(max = 45, message = "La dirección no puede exceder los 45 caracteres")
    private String direccion;

    @Size(min = 8, max = 9, message = "El teléfono debe tener 8 o 9 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El teléfono solo debe contener números")
    private String telefono;
}