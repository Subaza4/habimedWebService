package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.cita.dto.CitaResponseDto;
import com.habimed.habimedWebService.cita.dto.CitaUpdateDto;

public interface CitaService {
    
    List<CitaResponseDto> getCitas(CitaRequest request);

    CitaResponseDto getCitaById(Integer id);

    Integer setCita(CitaRequest cita);

    Boolean deleteCita(CitaRequest request);

    CitaResponseDto updateCita(Integer id, CitaUpdateDto cita);
}
