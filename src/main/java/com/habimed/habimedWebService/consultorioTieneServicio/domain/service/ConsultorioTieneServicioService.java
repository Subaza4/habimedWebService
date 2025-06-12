package com.habimed.habimedWebService.consultorioTieneServicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.consultorioTieneServicio.domain.model.ConsultorioTieneServicio;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;

public interface ConsultorioTieneServicioService {
    
    List<ConsultorioTieneServicio> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request);

    List<ConsultorioTieneServicio> getConsultoriosServiciosByIdConsultorio(Integer idConsultorio);

    List<ConsultorioTieneServicio> getConsultoriosServiciosByIdServicio(Integer idServicio);

    ConsultorioTieneServicio addConsultorioTieneServicio(ConsultorioTieneServicioRequest request);

    //ConsultorioTieneServicio updateConsultoriosServicios(ConsultorioTieneServicioRequest );
    
    Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request);
}
