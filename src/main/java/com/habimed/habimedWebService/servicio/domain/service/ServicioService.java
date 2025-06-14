package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.dto.*;

public interface ServicioService {
    List<Servicio> findAll();
    List<Servicio> findAllWithConditions(ServicioFilterDto servicioFilterDto);
    ServicioResponseDto getById(Integer id);
    ServicioResponseDto save(ServicioInsertDto servicioInsertDto);
    Boolean delete(Integer id);
    ServicioResponseDto update(Integer id, ServicioUpdateDto servicioUpdateDto);
}
