package com.habimed.habimedWebService.receta.repository;

import com.habimed.habimedWebService.receta.domain.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {

}
