package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaResponse;

public interface CitaService {
    
    List<CitaResponse> getCitas(CitaRequest request);

    CitaResponse getCita(Integer id);

    Integer setCita(CitaRequest cita);

    boolean deleteCita(Integer id);
    
}
