package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.cita.repository.CitaRepository;

@Service
public class CitaServiceImpl implements CitaService{
    
    private CitaRepository citaRepository;
    
    @Autowired
    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public List<CitaDTO> getCitas(CitaRequest request) {
        return citaRepository.getCitas(request);
    }

    @Override
    public CitaDTO getCita(Integer id) {
        return citaRepository.getCita(id);
    }

    @Override
    public Integer setCita(CitaRequest cita) {
        return citaRepository.setCita(cita);
    }

    @Override
    public boolean deleteCita(Integer id) {
        return citaRepository.deleteCita(id);
    }
}
