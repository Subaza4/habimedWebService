package com.habimed.habimedWebService.recomendacion.dto;

import lombok.Data;

@Data
public class RecomendacionInsertDto {
    private Integer idrecomendacion;
    private Integer idcita;
    private String descripcion;
}
