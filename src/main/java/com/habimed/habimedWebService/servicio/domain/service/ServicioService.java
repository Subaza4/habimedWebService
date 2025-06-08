package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.servicio.dto.ServicioDTO;
import com.habimed.habimedWebService.servicio.dto.ServicioRequest;

public interface ServicioService {
    
    List<ServicioDTO> getAllServicios(ServicioRequest request);

    Integer setServicio(ServicioRequest request);

    boolean deleteServicio(Integer idservicio);
}
