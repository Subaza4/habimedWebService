package com.habimed.habimedWebService.consultorio.dto;

import lombok.Data;

@Data
public class ConsultorioUpdateDto {
    private Integer idconsultorio;
    private String ruc;
    private String nombre;
    private String ubicacion;
    private String direccion;
    private String telefono;
}
