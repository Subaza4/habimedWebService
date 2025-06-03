package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.parameter.response.DoctorTrabajaConsultorioResponse;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.parameter.request.DoctorTrabajaConsultorioRequest;

public interface DoctorTrabajaConsultorioService {

    List<DoctorTrabajaConsultorioResponse> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
    DoctorTrabajaConsultorioResponse getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);

    Integer createDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);

    boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
}
