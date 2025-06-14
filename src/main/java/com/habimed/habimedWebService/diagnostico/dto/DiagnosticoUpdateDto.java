package com.habimed.habimedWebService.diagnostico.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiagnosticoUpdateDto {
    private Integer idDiagnostico;
    private Integer idCita;
    private String descripcion;
    private LocalDate fechaDiagnostico;
}
