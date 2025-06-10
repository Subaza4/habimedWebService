package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;

public interface CitaService {
    
    List<CitaDTO> getCitas(CitaRequest request);

    CitaDTO getCita(Integer id);

    Integer setCita(CitaRequest cita);

    boolean deleteCita(CitaRequest request);
    
}
