package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioResponse;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.repository.DoctorTrabajaConsultorioRepository;

@Service
public class DoctorTrabajaConsultorioServiceImpl implements DoctorTrabajaConsultorioService {

    private DoctorTrabajaConsultorioRepository doctorTrabajaConsultorioRepository;
    
    @Override
    public List<DoctorTrabajaConsultorioResponse> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.getAllDoctorsTrabajaConsultorio(request);
    }

    @Override
    public DoctorTrabajaConsultorioResponse getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.getDoctorTrabajaConsultorio(request);
    }

    @Override
    public Integer createDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.createDoctorTrabajaConsultorio(request);
    }

    @Override
    public boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.deleteDoctorTrabajaConsultorio(request);
    }
    

}
