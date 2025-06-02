package com.habimed.habimedWebService.consultorio.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorio.domain.parameter.consultorioRequest.ConsultorioRequest;
import com.habimed.habimedWebService.consultorio.repository.ConsultorioRepository;
import com.habimed.habimedWebService.consultorio.repository.dto.ConsultorioDTO;

@Service
public class ConsultorioServiceImpl implements ConsultorioService {

    private ConsultorioRepository consultorioRepository;

    @Autowired
    private ConsultorioServiceImpl(ConsultorioRepository consultorioRepository) {
        this.consultorioRepository = consultorioRepository;
    }
    // Aquí puedes implementar los métodos de ConsultorioService
    // Por ejemplo, podrías tener un repositorio inyectado para acceder a la base de datos

    @Override
    public List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request) {
        // Implementación para obtener todos los consultorios
        List<ConsultorioDTO> consultorios = consultorioRepository.getAllConsultorios(request);

        return consultorios;
    }

    @Override
    public ConsultorioDTO getConsultorioById(Integer id) {
        // Implementación para obtener un consultorio por ID
        ConsultorioDTO consultorio = consultorioRepository.getConsultorio(id);
        return consultorio;
    }

    @Override
    public Integer setConsultorio(ConsultorioRequest request) {
        // Implementación para crear un nuevo consultorio
        Integer idConsultorio = consultorioRepository.setConsultorio(request);
        return idConsultorio;
    }

    @Override
    public boolean updateConsultorio(ConsultorioRequest request) {
        // Implementación para actualizar un consultorio existente
        boolean isUpdated = consultorioRepository.updateConsultorio(request);
        return isUpdated;
    }

    @Override
    public boolean deleteConsultorio(Integer id) {
        // Implementación para eliminar un consultorio por ID
        boolean isDeleted = consultorioRepository.deleteConsultorio(id);
        return isDeleted;
    }
    
}
