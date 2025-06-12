package com.habimed.habimedWebService.detallePago.domain.service;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoCreateDto;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;

import java.util.List;

public interface DetallePagoService {

    List<DetallePagoCreateDto> getDetallePago(DetallePagoRequest request);
    
    Integer setDetallePago(DetallePagoRequest request);

    Boolean deleteDetallePago(Integer id);

}
