package com.habimed.habimedWebService.detallePago.domain.service;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoDTO;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;

import java.util.List;

public interface DetallePagoService {

    List<DetallePagoDTO> getDetallePago(DetallePagoRequest request);
    
    Integer setDetallePago(DetallePagoRequest request);

    boolean deleteDetallePago(DetallePagoRequest request);

}
