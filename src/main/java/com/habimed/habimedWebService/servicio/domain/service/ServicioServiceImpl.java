package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.repository.EspecialidadRepository;
import com.habimed.habimedWebService.servicio.dto.ServicioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.servicio.dto.ServicioRequest;
import com.habimed.habimedWebService.servicio.repository.ServicioRepository;

@Service
public class ServicioServiceImpl implements ServicioService{
    
    private final ServicioRepository servicioRepository;
    private final EspecialidadRepository especialidadRepository;

    @Autowired
    public ServicioServiceImpl(ServicioRepository servicioRepository, EspecialidadRepository especialidadRepository) {
        this.servicioRepository = servicioRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public List<ServicioDTO> getAllServicios(ServicioRequest request) {
        return servicioRepository.getAllServicios(request);
    }

    @Override
    public Integer setServicio(ServicioRequest request) {
        Especialidad especialidad = this.especialidadRepository.getEspecialidad(request.getIdespecialidad());
        if(especialidad.getIdespecialidad() <= 0){
            return 0;
        }
        return servicioRepository.setServicio(request);
    }

    @Override
    public boolean deleteServicio(Integer idservicio) {
        return servicioRepository.deleteServicio(idservicio);
    }
}
