package com.habimed.habimedWebService.consultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.dto.*;

public interface ConsultorioService {
    List<Consultorio> findAll();
    List<Consultorio> findAllWithConditions(ConsultorioFilterDto consultorioFilterDto);
    ConsultorioResponseDto getById(Integer id);
    ConsultorioResponseDto save(ConsultorioInsertDto consultorioInsertDto);
    Boolean delete(Integer id);
    ConsultorioResponseDto update(Integer id, ConsultorioUpdateDto consultorioUpdateDto);
}
