package com.habimed.habimedWebService.diagnostico.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoResponseDto;
import lombok.RequiredArgsConstructor;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;
import com.habimed.habimedWebService.diagnostico.repository.DiagnosticoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiagnosticoServiceImpl implements DiagnosticoService {
    
    private final DiagnosticoRepository diagnosticoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DiagnosticoDTO> getAllDiagnosticosWithDetails(DiagnosticoRequest request) {
        return diagnosticoRepository.getAllDiagnosticos(request);
    }

    @Override
    public DiagnosticoDTO getDiagnosticoByIdWithDetails(Integer idDiagnostico) {
        return diagnosticoRepository.getDiagnosticoById(idDiagnostico);
    }

    @Override
    public DiagnosticoResponseDto getDiagnosticoByIdDiagnostico(Integer idDiagnostico){
        Diagnostico diagnostico = diagnosticoRepository.getById(idDiagnostico);
        return modelMapper.map(diagnostico, DiagnosticoResponseDto.class);
    };

    @Override
    public List<DiagnosticoResponseDto> getDiagnosticoByIdCita(Integer idCita){
        List<Diagnostico> diagnosticos = diagnosticoRepository.getByIdCita(idCita);
        return diagnosticos.stream()
                .map(x -> modelMapper.map(x, DiagnosticoResponseDto.class)).collect(Collectors.toList());
    };

    @Override
    public DiagnosticoResponseDto addDiagnostico(DiagnosticoRequest request) {
        Integer id =  diagnosticoRepository.setDiagnostico(request);
        return this.getDiagnosticoByIdDiagnostico(id);
    }

    @Override
    public DiagnosticoResponseDto updateDiagnostico(Integer idDiagnostico, DiagnosticoRequest request){
        return modelMapper(diagnosticoRepository.update(idDiagnostico, request) , DiagnosticoResponseDto.class);
    }

    @Override
    public Boolean deleteDiagnostico(Integer iddiagnostico) {
        return diagnosticoRepository.deleteDiagnostico(iddiagnostico);
    }
}
