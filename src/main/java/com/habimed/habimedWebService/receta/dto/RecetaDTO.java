package com.habimed.habimedWebService.receta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetaDTO {
    private Long idReceta;
    private Long idCita;
    private String descripcion;
    private LocalDate fechaReceta;
}
