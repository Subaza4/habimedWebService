package com.habimed.habimedWebService.diagnostico.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiagnosticoDeleteDto {
    private Integer idDiagnostico;
    private Integer idCita;
    private String descripcion;
    private LocalDate fechaDiagnostico;
}
