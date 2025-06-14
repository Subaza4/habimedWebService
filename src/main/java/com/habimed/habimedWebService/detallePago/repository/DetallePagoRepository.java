package com.habimed.habimedWebService.detallePago.repository;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePagoRepository extends JpaRepository<DetallePago, Integer> {

}