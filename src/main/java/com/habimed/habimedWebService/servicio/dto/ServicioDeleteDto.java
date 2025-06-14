package com.habimed.habimedWebService.servicio.dto;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import lombok.Data;

import java.util.List;

@Data
public class ServicioDeleteDto {
    private Integer idServicio;
    private Especialidad especialidad;
    private String nombre;
    private String descripcion;
    private String riesgos;
    private List<Consultorio> consultorios;
}
