package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.servicio.dto.ServicioRequest;
import com.habimed.habimedWebService.servicio.dto.ServicioResponse;

public interface ServicioService {
    
    List<ServicioResponse> getAllServicios(ServicioRequest request);

    Integer setServicio(ServicioRequest request);

    boolean deleteServicio(Integer idservicio);
}
