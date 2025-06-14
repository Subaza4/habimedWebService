package com.habimed.habimedWebService.cita.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CitaRepository extends JpaRepository<Cita, Integer> {

}