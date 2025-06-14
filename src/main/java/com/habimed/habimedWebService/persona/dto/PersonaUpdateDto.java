package com.habimed.habimedWebService.persona.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class PersonaUpdateDto {
    @Size(max = 45, message = "Los nombres no pueden exceder 45 caracteres")
    private String nombres;

    @Size(max = 45, message = "Los apellidos no pueden exceder 45 caracteres")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 45, message = "El correo no puede exceder 45 caracteres")
    private String correo;

    @Pattern(regexp = "^\\d{9}$", message = "El celular debe tener 9 dígitos")
    private String celular;

    @Size(max = 45, message = "La dirección no puede exceder 45 caracteres")
    private String direccion;

    private LocalDate fechaNacimiento;
}