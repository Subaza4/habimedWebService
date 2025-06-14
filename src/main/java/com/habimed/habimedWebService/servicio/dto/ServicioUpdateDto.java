package com.habimed.habimedWebService.servicio.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class ServicioUpdateDto {
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 500, message = "Los riesgos no pueden exceder 500 caracteres")
    private String riesgos;
}