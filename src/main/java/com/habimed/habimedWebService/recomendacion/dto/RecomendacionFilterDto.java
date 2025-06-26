package com.habimed.habimedWebService.recomendacion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionFilterDto {
    private Integer idCita;
    private LocalDate fechaRecomendacionInicio;
    private LocalDate fechaRecomendacionFin;
    private String descripcionContiene;
}