package com.habimed.habimedWebService.receta.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaFilterDto {
    private Integer idCita;
    private LocalDate fechaRecetaInicio;
    private LocalDate fechaRecetaFin;
    private String descripcionContiene;
}