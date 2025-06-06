package com.habimed.habimedWebService.consultorioTieneServicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;

public interface ConsultorioTieneServicioService {
    
    List<ConsultorioTieneServicioDTO> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request);

    ConsultorioTieneServicioDTO getConsultoriosServiciosById(ConsultorioTieneServicioRequest request);

    Integer setConsultorioTieneServicio(ConsultorioTieneServicioRequest request);
    
    Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request);
}
