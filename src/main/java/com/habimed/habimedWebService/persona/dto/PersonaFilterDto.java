package com.habimed.habimedWebService.persona.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonaFilterDto {
    private Long dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private LocalDate fechaNacimiento;
}