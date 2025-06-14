package com.habimed.habimedWebService.detallePago.domain.service;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.detallePago.dto.*;

import java.util.List;

public interface DetallePagoService {
    List<DetallePago> findAll();
    List<DetallePago> findAllWithConditions(DetallePagoFilterDto detallePagoFilterDto);
    DetallePagoResponseDto getById(Integer id);
    DetallePagoResponseDto save(DetallePagoInsertDto detallePagoInsertDto);
    Boolean delete(Integer id);
    DetallePagoResponseDto update(Integer id, DetallePagoUpdateDto detallePagoUpdateDto);
}