package com.habimed.habimedWebService.diagnostico.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoInsertDto {
    @NotNull(message = "El ID de la cita es obligatorio")
    private Integer idCita;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion;
}
