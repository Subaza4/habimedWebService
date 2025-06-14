package com.habimed.habimedWebService.diagnostico.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoResponseDto {
    private Integer idDiagnostico;
    private Integer idCita;
    private String descripcion;
    private LocalDate fechaDiagnostico;
    private String nombreDoctor;
    private String nombrePaciente;
    private String motivoCita;
}