package com.habimed.habimedWebService.recomendacion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recomendacion {
    private Integer idrecomendacion;
    private Integer idcita;
    private String descripcion;
    private Timestamp fecha_recomendacion;
}
