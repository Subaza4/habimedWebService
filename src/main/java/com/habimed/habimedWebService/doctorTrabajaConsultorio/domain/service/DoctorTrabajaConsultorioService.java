package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioResponseDto;

public interface DoctorTrabajaConsultorioService {

    List<DoctorTrabajaConsultorioDTO> getAllDoctorsTrabajaConsultorioWithDetails(DoctorTrabajaConsultorioRequest request);

    List<DoctorTrabajaConsultorioResponseDto> findAllDoctorsTrabajaConsultorio();

    List<DoctorTrabajaConsultorioResponseDto> getDoctorConsultorioByIdConsultorio(Integer idConsultorio);

    List<DoctorTrabajaConsultorioResponseDto> getDoctorConsultorioByIdDoctor(Integer idDoctor);

    DoctorTrabajaConsultorioResponseDto addDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);

    Boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request);
    
}
