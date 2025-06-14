package com.habimed.habimedWebService.receta.repository;

import com.habimed.habimedWebService.receta.domain.model.Receta;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecetaRepository extends JpaRepository<Receta, Integer> {

}
