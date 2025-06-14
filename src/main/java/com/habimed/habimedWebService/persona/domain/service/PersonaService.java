package com.habimed.habimedWebService.persona.domain.service;

import java.util.List;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.*;

public interface PersonaService {
    List<Persona> findAll();
    List<Persona> findAllWithConditions(PersonaFilterDto personaFilterDto);
    PersonaResponseDto getById(Long dni);
    PersonaResponseDto save(PersonaInsertDto personaInsertDto);
    Boolean delete(Long dni);
    PersonaResponseDto update(Long dni, PersonaUpdateDto personaUpdateDto);
}
