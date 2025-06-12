package com.habimed.habimedWebService.consultorioTieneServicio.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.habimed.habimedWebService.consultorioTieneServicio.domain.model.ConsultorioTieneServicio;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import com.habimed.habimedWebService.consultorioTieneServicio.repository.ConsultorioTieneServicioRepository;

@Service
@RequiredArgsConstructor
public class ConsultorioTieneServicioServiceImpl implements ConsultorioTieneServicioService{
    
    private final ConsultorioTieneServicioRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<ConsultorioTieneServicio> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request) {
        return repository.getAllConsultoriosServicios(request)
                .stream().map(entity -> modelMapper.map(entity, ConsultorioTieneServicio.class)).collect(Collectors.toList());
    }

    @Override
    public List<ConsultorioTieneServicio> getConsultoriosServiciosByIdConsultorio(Integer idConsultorio) {
        return repository.getConsultorioServicioByIdServicio(request);
    }

    @Override
    public List<ConsultorioTieneServicio> getConsultoriosServiciosByIdServicio(Integer idServicio) {
        return repository.getConsultorioServicioByIdServicio(request);
    }

    @Override
    public ConsultorioTieneServicio addConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        return repository.setConsultorioTieneServicio(request);
    }



    @Override
    public Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        return repository.deleteConsultorioTieneServicio(request);
    }
}
