package com.habimed.habimedWebService.resenia.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReseniaFilterDto {
    private Integer idDoctor;
    private BigDecimal calificacionMinima;
    private BigDecimal calificacionMaxima;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
