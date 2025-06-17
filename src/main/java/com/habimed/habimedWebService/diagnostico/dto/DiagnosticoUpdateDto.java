package com.habimed.habimedWebService.diagnostico.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class DiagnosticoUpdateDto {
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
}