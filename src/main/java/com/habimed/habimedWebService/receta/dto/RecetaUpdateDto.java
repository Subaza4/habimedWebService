package com.habimed.habimedWebService.receta.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecetaUpdateDto {
    private Long idReceta;
    private Long idCita;
    private String descripcion;
    private LocalDate fechaReceta;
}
