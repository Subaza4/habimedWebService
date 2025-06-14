package com.habimed.habimedWebService.permisoHistorial.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PermisoHistorialFilterDto {
    private Integer idpermisohistorial;
    private Integer iddoctor;
    private Integer idpaciente;
    private LocalDate fechaotorgapermiso;
    private LocalDate fechadeniegapermiso;
    private Boolean estado;
}
