package com.habimed.habimedWebService.horarioDoctor.domain.service;

import java.util.List;

import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorRequest;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorResponse;

public interface HorarioDoctorService {

    List<HorarioDoctorResponse> getAllHorarios(HorarioDoctorRequest request);
    
    HorarioDoctorResponse getHorarioById(Integer idhorariodoctor);

    Integer setHorario(HorarioDoctorRequest request);

    boolean deleteHorario(Integer idhorariodoctor);

}
