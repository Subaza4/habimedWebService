package com.habimed.habimedWebService.persona.domain.parameter.personaRequest;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRequest {
    private Long dni;
    private String nombre;
    private String apellidos;
    private String correo;
    private String celular;
    private String direccion;
    //@Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fecha_nacimiento;
}