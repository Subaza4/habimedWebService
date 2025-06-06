package com.habimed.habimedWebService.diagnostico.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diagnostico {
    private Integer iddiagnostico;
    private Integer idcita;
    private String descripcion;
    private LocalDate fecha_diagnostico;
}