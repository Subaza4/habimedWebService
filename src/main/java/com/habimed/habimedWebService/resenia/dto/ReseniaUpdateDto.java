package com.habimed.habimedWebService.resenia.dto;

import lombok.Data;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.math.BigDecimal;

@Data
public class ReseniaUpdateDto {
    @DecimalMin(value = "0.0", message = "La calificación debe ser mayor o igual a 0")
    @DecimalMax(value = "5.0", message = "La calificación debe ser menor o igual a 5")
    private Double calificacion;

    @Size(max = 1000, message = "El comentario no puede exceder 1000 caracteres")
    private String comentario;
}
