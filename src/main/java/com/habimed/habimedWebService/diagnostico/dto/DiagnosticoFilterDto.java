package com.habimed.habimedWebService.diagnostico.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoFilterDto {
    private Integer idCita;
    private LocalDate fechaDiagnosticoInicio;
    private LocalDate fechaDiagnosticoFin;
    private String descripcionContiene;
}
