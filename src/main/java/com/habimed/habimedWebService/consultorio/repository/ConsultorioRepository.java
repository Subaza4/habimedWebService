package com.habimed.habimedWebService.consultorio.repository;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {
    
}