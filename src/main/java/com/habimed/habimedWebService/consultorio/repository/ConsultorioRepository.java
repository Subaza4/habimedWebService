package com.habimed.habimedWebService.consultorio.repository;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConsultorioRepository extends JpaRepository<Consultorio, Integer> {
    
}