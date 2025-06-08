package com.habimed.habimedWebService.consultorioTieneServicio.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import com.habimed.habimedWebService.consultorioTieneServicio.repository.ConsultorioTieneServicioRepository;

@Service
public class ConsultorioTieneServicioServiceImpl implements ConsultorioTieneServicioService{
    
    private final ConsultorioTieneServicioRepository repository;
    
    @Autowired
    public ConsultorioTieneServicioServiceImpl(ConsultorioTieneServicioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ConsultorioTieneServicioDTO> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request) {
        return repository.getAllConsultoriosServicios(request);
    }

    /*@Override
    public ConsultorioTieneServicioDTO getConsultoriosServiciosById(ConsultorioTieneServicioRequest request) {
        return repository.getConsultorioServicioById(request);
    }*/

    @Override
    public Integer setConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        return repository.setConsultorioTieneServicio(request);
    }

    @Override
    public Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        return repository.deleteConsultorioTieneServicio(request);
    }
}
