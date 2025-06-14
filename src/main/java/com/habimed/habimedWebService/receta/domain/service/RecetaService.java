package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.receta.dto.*;

import java.util.List;

public interface RecetaService {
    List<Receta> findAll();
    List<Receta> findAllWithConditions(RecetaFilterDto recetaFilterDto);
    RecetaResponseDto getById(Integer id);
    RecetaResponseDto save(RecetaInsertDto recetaInsertDto);
    Boolean delete(Integer id);
    RecetaResponseDto update(Integer id, RecetaUpdateDto recetaUpdateDto);
}