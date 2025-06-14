package com.habimed.habimedWebService.especialidad.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadResponseDto {
    private Integer idEspecialidad;
    private String nombre;
    private String descripcion;
    private Integer cantidadServicios;
}