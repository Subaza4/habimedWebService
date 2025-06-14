package com.habimed.habimedWebService.recomendacion.dto;

import lombok.Data;

@Data
public class RecomendacionUpdateDto {
    private Integer idrecomendacion;
    private Integer idcita;
    private String descripcion;
}
