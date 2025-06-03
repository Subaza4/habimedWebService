package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.servicio.domain.parameter.request.ServicioRequest;
import com.habimed.habimedWebService.servicio.domain.parameter.response.ServicioResponse;
import com.habimed.habimedWebService.servicio.repository.ServicioRepository;

@Service
public class ServicioServiceImpl implements ServicioService{
    
    private final ServicioRepository servicioRepository;

    @Autowired
    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<ServicioResponse> getAllServicios(ServicioRequest request) {
        return servicioRepository.getAllServicios(request);
    }

    @Override
    public Integer setServicio(ServicioRequest request) {
        return servicioRepository.setServicio(request);
    }

    @Override
    public boolean deleteServicio(Integer idservicio) {
        return servicioRepository.deleteServicio(idservicio);
    }
}
