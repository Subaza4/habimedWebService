package com.habimed.habimedWebService.consultorio.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;
import com.habimed.habimedWebService.consultorio.repository.ConsultorioRepository;

@Service
public class ConsultorioServiceImpl implements ConsultorioService {

    private ConsultorioRepository consultorioRepository;

    @Autowired
    private ConsultorioServiceImpl(ConsultorioRepository consultorioRepository) {
        this.consultorioRepository = consultorioRepository;
    }

    @Override
    public List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request) {
        // Implementación para obtener todos los consultorios
        return consultorioRepository.getAllConsultorios(request);
    }

    @Override
    public ConsultorioDTO getConsultorioById(Integer id) {
        // Implementación para obtener un consultorio por ID
        return consultorioRepository.getConsultorio(id);
    }

    @Override
    public Integer setConsultorio(ConsultorioRequest request) {
        // Implementación para crear un nuevo consultorio
        return consultorioRepository.setConsultorio(request);
    }

    @Override
    public boolean deleteConsultorio(Integer id) {
        if( id == null ){
            return false;
        }else{
            return consultorioRepository.deleteConsultorio(id);
        }
    }
    
}
