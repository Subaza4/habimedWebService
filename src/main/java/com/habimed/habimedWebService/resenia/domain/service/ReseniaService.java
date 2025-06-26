package com.habimed.habimedWebService.resenia.domain.service;

import com.habimed.habimedWebService.resenia.domain.model.Resenia;
import com.habimed.habimedWebService.resenia.dto.*;

import java.util.List;

public interface ReseniaService {
    List<Resenia> findAll();
    List<Resenia> findAllWithConditions(ReseniaFilterDto reseniaFilterDto);
    ReseniaResponseDto getById(Integer id);
    ReseniaResponseDto save(ReseniaInsertDto reseniaInsertDto);
    Boolean delete(Integer id);
    ReseniaResponseDto update(Integer id, ReseniaUpdateDto reseniaUpdateDto);
}