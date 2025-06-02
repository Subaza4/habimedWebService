package com.habimed.habimedWebService.consultorio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultorio {

    private Integer idconsultorio;

    private String ruc;

    private String nombre;

    private String ubicacion;

    private String direccion;

    private String telefono;
}