package com.habimed.habimedWebService.detallePago.domain.service;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoDTO;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;

public interface DetallePagoService {
    
    DetallePagoDTO getDetallePago(DetallePagoRequest request);
    
    Integer setDetallePago(DetallePagoRequest request);

    boolean deleteDetallePago(DetallePagoRequest request);

}
