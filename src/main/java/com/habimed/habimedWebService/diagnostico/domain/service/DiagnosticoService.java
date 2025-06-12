package com.habimed.habimedWebService.diagnostico.domain.service;

import java.util.List;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoResponseDto;

public interface DiagnosticoService {
    
    List<DiagnosticoDTO> getAllDiagnosticosWithDetails(DiagnosticoRequest request);

    DiagnosticoDTO getDiagnosticoByIdWithDetails(Integer idDiagnostico);

    DiagnosticoResponseDto getDiagnosticoByIdDiagnostico(Integer idDiagnostico);

    List<DiagnosticoResponseDto> getDiagnosticoByIdCita(Integer idCita);

    DiagnosticoResponseDto addDiagnostico(DiagnosticoRequest request);

    DiagnosticoResponseDto updateDiagnostico(Integer idDiagnostico, DiagnosticoRequest request);
    
    Boolean deleteDiagnostico(Integer iddiagnostico);
}
