package com.habimed.habimedWebService.especialidad.domain.service;

import java.util.List;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.domain.parameter.especialidadRequest.EspecialidadRequest;

public interface EspecialidadService {
    
    List<Especialidad> getEspecialidades();

    Especialidad getEspecialidad(Integer idEspecialidad);

    Integer setEspecialidad(EspecialidadRequest especialidad);
    
    Integer updateEspecialidad(EspecialidadRequest especialidad);

    boolean deleteEspecialidad(Integer idEspecialidad);
}
