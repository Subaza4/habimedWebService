package com.habimed.habimedWebService.especialidad.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadRequest;
import com.habimed.habimedWebService.especialidad.repository.EspecialidadRepository;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {
    
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especialidad> getEspecialidades(){
        return especialidadRepository.getEspecialidades();
    }

    @Override
    public Especialidad getEspecialidad(Integer idEspecialidad) {
        return especialidadRepository.getEspecialidad(idEspecialidad);
    }

    @Override
    public Integer setEspecialidad(EspecialidadRequest request) {
        return especialidadRepository.setEspecialidad(request);
    }

    @Override
    public Integer updateEspecialidad(EspecialidadRequest request) {
        return especialidadRepository.setEspecialidad(request);
    }

    @Override
    public boolean deleteEspecialidad(Integer idEspecialidad) {
        return especialidadRepository.deleteEspecialidad(idEspecialidad);
    }
}
