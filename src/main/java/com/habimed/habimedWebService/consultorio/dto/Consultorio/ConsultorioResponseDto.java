package com.habimed.habimedWebService.consultorio.dto.Consultorio;

import lombok.Data;

@Data
public class ConsultorioResponseDto {
    private Integer idConsultorio;
    private String ruc;
    private String nombre;
    private String ubicacion;
    private String direccion;
    private String telefono;
}