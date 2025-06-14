package com.habimed.habimedWebService.horarioDoctor.repository;

import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HorarioDoctorRepository extends JpaRepository<HorarioDoctor, Integer> {

}