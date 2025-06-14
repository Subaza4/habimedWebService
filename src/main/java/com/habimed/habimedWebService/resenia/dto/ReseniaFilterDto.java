package com.habimed.habimedWebService.resenia.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReseniaFilterDto {
    private Integer idDoctor;
    private Double calificacionMinima;
    private Double calificacionMaxima;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
