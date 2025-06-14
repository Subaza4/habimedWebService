package com.habimed.habimedWebService.recomendacion.domain.service;

import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import com.habimed.habimedWebService.recomendacion.dto.*;

import java.util.List;

public interface RecomendacionService {
    List<Recomendacion> findAll();
    List<Recomendacion> findAllWithConditions(RecomendacionFilterDto recomendacionFilterDto);
    RecomendacionResponseDto getById(Integer id);
    RecomendacionResponseDto save(RecomendacionInsertDto recomendacionInsertDto);
    Boolean delete(Integer id);
    RecomendacionResponseDto update(Integer id, RecomendacionUpdateDto recomendacionUpdateDto);
}