package com.habimed.habimedWebService.horarioDoctor.domain.service;

import java.util.List;

import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorRequest;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorDTO;

public interface HorarioDoctorService {

    List<HorarioDoctorDTO> getAllHorarios(HorarioDoctorRequest request);
    
    HorarioDoctorDTO getHorarioById(Integer idhorariodoctor);

    Integer setHorario(HorarioDoctorRequest request);

    boolean deleteHorario(Integer idhorariodoctor);

}
