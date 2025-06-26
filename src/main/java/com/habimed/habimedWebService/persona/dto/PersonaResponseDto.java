package com.habimed.habimedWebService.persona.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonaResponseDto {
    private Long dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private String celular;
    private String direccion;
    private LocalDate fechaNacimiento;
}
