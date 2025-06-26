package com.habimed.habimedWebService.consultorio.dto;


import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ConsultorioDTO {
    private Integer idconsultorio;

    private String ruc;

    private String nombre;

    private String ubicacion;

    private String direccion;

    private String telefono;

}
