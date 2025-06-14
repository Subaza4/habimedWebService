package com.habimed.habimedWebService.persona.domain.model;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @Column(name = "dni")
    private Long dni;

    @Column(name = "nombres", nullable = false, length = 45)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 45)
    private String apellidos;

    @Column(name = "celular", length = 9)
    private String celular;

    @Column(name = "direccion", length = 45)
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
}
