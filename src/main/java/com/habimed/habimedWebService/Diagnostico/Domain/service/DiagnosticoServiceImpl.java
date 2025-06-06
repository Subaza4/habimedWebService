package com.habimed.habimedWebService.diagnostico.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;
import com.habimed.habimedWebService.diagnostico.repository.DiagnosticoRepository;

public class DiagnosticoServiceImpl implements DiagnosticoService {
    
    private DiagnosticoRepository diagnosticoRepository;
    
    @Autowired
    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository) {
        this.diagnosticoRepository = diagnosticoRepository;
    }

    public List<DiagnosticoDTO> getAllDiagnosticos(DiagnosticoRequest request) {
        return diagnosticoRepository.getAllDiagnosticos(request);
    }

    public DiagnosticoDTO getDiagnosticoById(Integer iddiagnostico) {
        return diagnosticoRepository.getDiagnosticoById(iddiagnostico);
    }

    public Integer setDiagnostico(DiagnosticoRequest request) {
        return diagnosticoRepository.setDiagnostico(request);
    }

    public boolean deleteDiagnostico(Integer iddiagnostico) {
        return diagnosticoRepository.deleteDiagnostico(iddiagnostico);
    }
}
