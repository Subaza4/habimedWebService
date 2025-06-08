package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;

public interface DoctorTrabajaConsultorioService {

    List<DoctorTrabajaConsultorioDTO> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
    /*DoctorTrabajaConsultorioResponse getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);*/

    Integer setDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);

    boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
}
