package com.habimed.habimedWebService.consultorio.domain.service;
import java.util.List;

import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;

public interface ConsultorioService {
    
    List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request);

    ConsultorioDTO getConsultorioById(Integer id);
    
    Integer setConsultorio(ConsultorioRequest request);

    Boolean deleteConsultorio(Integer id);
    
}
