package com.habimed.habimedWebService.detallePago.domain.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoDTO;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;
import com.habimed.habimedWebService.detallePago.repository.DetallePagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallePagoServiceImpl implements DetallePagoService {

    private final DetallePagoRepository detallePagoRepository;

    @Autowired
    public DetallePagoServiceImpl(DetallePagoRepository detallePagoRepository) {
        this.detallePagoRepository = detallePagoRepository;
    }

    @Override
    public List<DetallePagoDTO> getDetallePago(DetallePagoRequest request) {
        // Implement the logic to retrieve all DetallePago based on the request
        return detallePagoRepository.getDetallePago(request);
    }

    @Override
    public Integer setDetallePago(DetallePagoRequest request) {
        // Implement the logic to set a DetallePago based on the request
        return detallePagoRepository.setDetallePago(request);
    }

    @Override
    public Boolean deleteDetallePago(DetallePagoRequest request) {
        // Implement the logic to delete a DetallePago based on the request
        return detallePagoRepository.deleteDetallePago(request);
    }
    
}
