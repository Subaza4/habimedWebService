package com.habimed.habimedWebService.horarioDoctor.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorRequest;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorResponse;
import com.habimed.habimedWebService.horarioDoctor.repository.HorarioDoctorRepository;

@Service
public class HorarioDoctorServiceImpl implements HorarioDoctorService {
    
    private HorarioDoctorRepository horarioDoctorRepository;
    
    @Autowired
    public HorarioDoctorServiceImpl(HorarioDoctorRepository horarioDoctorRepository) {
        this.horarioDoctorRepository = horarioDoctorRepository;
    }

    @Override
    public List<HorarioDoctorResponse> getAllHorarios(HorarioDoctorRequest request) {
        return this.horarioDoctorRepository.getAllHorarios(request);
    }

    @Override
    public HorarioDoctorResponse getHorarioById(Integer idhorariodoctor) {
        return this.horarioDoctorRepository.getHorarioById(idhorariodoctor);
    }

    @Override
    public Integer setHorario(HorarioDoctorRequest request) {
        return this.horarioDoctorRepository.setHorario(request);
    }
    
    @Override
    public boolean deleteHorario(Integer idhorariodoctor) {
        return this.horarioDoctorRepository.deleteHorario(idhorariodoctor);
    }
}
