package com.habimed.habimedWebService.diagnostico.domain.service;

import java.util.List;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.diagnostico.dto.*;

public interface DiagnosticoService {
    List<Diagnostico> findAll();
    List<Diagnostico> findAllWithConditions(DiagnosticoFilterDto diagnosticoFilterDto);
    DiagnosticoResponseDto getById(Integer id);
    DiagnosticoResponseDto save(DiagnosticoInsertDto diagnosticoInsertDto);
    Boolean delete(Integer id);
    DiagnosticoResponseDto update(Integer id, DiagnosticoUpdateDto diagnosticoUpdateDto);
}