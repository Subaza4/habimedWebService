package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.repository.DoctorTrabajaConsultorioRepository;

@Service
public class DoctorTrabajaConsultorioServiceImpl implements DoctorTrabajaConsultorioService {

    private DoctorTrabajaConsultorioRepository doctorTrabajaConsultorioRepository;
    
    @Override
    public List<DoctorTrabajaConsultorioDTO> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.getAllDoctorsTrabajaConsultorio(request);
    }

    /*@Override
    public DoctorTrabajaConsultorioResponse getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.getDoctorTrabajaConsultorio(request);
    }*/

    @Override
    public Integer setDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.setDoctorTrabajaConsultorio(request);
    }

    @Override
    public Boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.deleteDoctorTrabajaConsultorio(request);
    }
    

}
