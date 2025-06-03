package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.servicio.domain.parameter.request.ServicioRequest;
import com.habimed.habimedWebService.servicio.domain.parameter.response.ServicioResponse;

public interface ServicioService {
    
    List<ServicioResponse> getAllServicios(ServicioRequest request);

    Integer setServicio(ServicioRequest request);

    boolean deleteServicio(Integer idservicio);
}
