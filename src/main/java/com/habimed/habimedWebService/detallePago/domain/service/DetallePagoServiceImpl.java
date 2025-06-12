package com.habimed.habimedWebService.detallePago.domain.service;

import lombok.RequiredArgsConstructor;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoCreateDto;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;
import com.habimed.habimedWebService.detallePago.repository.DetallePagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePagoServiceImpl implements DetallePagoService {

    private final DetallePagoRepository detallePagoRepository;

    @Override
    public List<DetallePagoCreateDto> getDetallePago(Deta request) {
        // Implement the logic to retrieve all DetallePago based on the request
        return detallePagoRepository.getDetallePago(request);
    }

    @Override
    public Integer setDetallePago(DetallePagoRequest request) {
        // Implement the logic to set a DetallePago based on the request
        return detallePagoRepository.setDetallePago(request);
    }

    @Override
    public Boolean deleteDetallePago(Integer id) {
        // Implement the logic to delete a DetallePago based on the request
        return detallePagoRepository.deleteDetallePago(id);
    }
    
}
