package com.habimed.habimedWebService.servicio.repository;

import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

}