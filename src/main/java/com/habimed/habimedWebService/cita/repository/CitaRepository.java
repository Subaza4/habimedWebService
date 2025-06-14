package com.habimed.habimedWebService.cita.repository;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository

public class CitaRepository extends JpaRepository<Cita, Integer> {

}