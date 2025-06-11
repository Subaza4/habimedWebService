package com.habimed.habimedWebService.receta.dto;

import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetaDTO {
    private Receta receta;
    private CitaDTO cita;
}
