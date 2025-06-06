package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioResponse;

public interface DoctorTrabajaConsultorioService {

    List<DoctorTrabajaConsultorioResponse> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
    DoctorTrabajaConsultorioResponse getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);

    Integer createDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);

    boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
}
