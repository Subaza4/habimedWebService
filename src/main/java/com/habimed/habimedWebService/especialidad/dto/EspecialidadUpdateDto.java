package com.habimed.habimedWebService.especialidad.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class EspecialidadUpdateDto {
    @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;
}