package com.habimed.habimedWebService.especialidad.domain.parameter.especialidadRequest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadRequest {

    private Integer idespecialidad;

    @Size(max = 45, message = "El nombre de la especialidad debe tener máximo 45 caracteres.")
    private String nombre;

    @Size(max = 255, message = "La descripción de la especialidad debe tener máximo 255 caracteres.")
    private String descripcion;
}
