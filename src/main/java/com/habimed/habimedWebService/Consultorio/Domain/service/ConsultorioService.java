package com.habimed.habimedWebService.consultorio.domain.service;
import java.util.List;

import com.habimed.habimedWebService.consultorio.domain.parameter.consultorioRequest.ConsultorioRequest;
import com.habimed.habimedWebService.consultorio.repository.dto.ConsultorioDTO;

public interface ConsultorioService {
    
    List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request);

    ConsultorioDTO getConsultorioById(Integer id);
    
    Integer setConsultorio(ConsultorioRequest request);

    boolean updateConsultorio(ConsultorioRequest request);

    boolean deleteConsultorio(Integer id);
    
}
