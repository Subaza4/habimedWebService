package com.habimed.habimedWebService.recomendacion.repository;

import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecomendacionRepository extends JpaRepository<Recomendacion, Integer> {

}
