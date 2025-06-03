package com.habimed.habimedWebService.servicio.domain.parameter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponse {
    private Integer idservicio;

    private String nombre;

    private String descripcion;

    private String riesgos;

    private Integer idespecialidad;

    private String especialidadNombre;

    private String especialidadDescripcion;

}
