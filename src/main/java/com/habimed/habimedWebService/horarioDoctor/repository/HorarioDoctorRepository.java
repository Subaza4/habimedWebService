package com.habimed.habimedWebService.horarioDoctor.repository;

import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioDoctorRepository extends JpaRepository<HorarioDoctor, Integer> {

}