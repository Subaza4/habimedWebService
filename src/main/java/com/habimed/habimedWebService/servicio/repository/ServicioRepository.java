package com.habimed.habimedWebService.servicio.repository;

import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServicioRepository extends JpaRepository<Servicio, Integer> {

}