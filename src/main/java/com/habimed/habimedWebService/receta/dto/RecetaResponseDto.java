package com.habimed.habimedWebService.receta.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaResponseDto {
    private Integer idReceta;
    private Integer idCita;
    private String descripcion;
    private LocalDate fechaReceta;
    private String nombreDoctor;
    private String nombrePaciente;
    private String motivoCita;
}