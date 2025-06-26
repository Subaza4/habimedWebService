package com.habimed.habimedWebService.persona.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class PersonaDeleteDto {
    private Long dni;
    private String nombre;
    private String apellidos;
    private String correo;
    private String celular;
    private String direccion;
    private Date fecha_nacimiento;
}
