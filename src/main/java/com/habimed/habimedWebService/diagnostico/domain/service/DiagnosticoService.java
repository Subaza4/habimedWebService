package com.habimed.habimedWebService.diagnostico.domain.service;

import java.util.List;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;

public interface DiagnosticoService {
    
    List<DiagnosticoDTO> getAllDiagnosticos(DiagnosticoRequest request);

    DiagnosticoDTO getDiagnosticoById(Integer iddiagnostico);

    Integer setDiagnostico(DiagnosticoRequest request);
    
    boolean deleteDiagnostico(Integer iddiagnostico);
}
