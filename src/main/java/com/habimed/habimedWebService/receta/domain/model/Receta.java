package com.habimed.habimedWebService.receta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receta {
    private Integer idreceta;
    private Integer idcita;
    private String descripcion;
    private String fecha_receta;
}
