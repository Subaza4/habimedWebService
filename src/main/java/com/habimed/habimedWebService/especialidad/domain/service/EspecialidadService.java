package com.habimed.habimedWebService.especialidad.domain.service;

import java.util.List;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.*;

public interface EspecialidadService {
    List<Especialidad> findAll();
    List<Especialidad> findAllWithConditions(EspecialidadFilterDto especialidadFilterDto);
    EspecialidadResponseDto getById(Integer id);
    EspecialidadResponseDto save(EspecialidadInsertDto especialidadInsertDto);
    Boolean delete(Integer id);
    EspecialidadResponseDto update(Integer id, EspecialidadUpdateDto especialidadUpdateDto);
}