package com.habimed.habimedWebService.recomendacion.repository;

import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Integer> {

}
