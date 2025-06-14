package com.habimed.habimedWebService.detallePago.repository;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DetallePagoRepository extends JpaRepository<DetallePago, Integer> {

}