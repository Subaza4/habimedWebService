package com.habimed.habimedWebService.horarioDoctor.domain.service;

import java.util.List;

import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import com.habimed.habimedWebService.horarioDoctor.dto.*;

public interface HorarioDoctorService {
    List<HorarioDoctor> findAll();
    List<HorarioDoctor> findAllWithConditions(HorarioDoctorFilterDto horarioDoctorFilterDto);
    HorarioDoctorResponseDto getById(Integer id);
    HorarioDoctorResponseDto save(HorarioDoctorInsertDto horarioDoctorInsertDto);
    Boolean delete(Integer id);
    HorarioDoctorResponseDto update(Integer id, HorarioDoctorUpdateDto horarioDoctorUpdateDto);
}
