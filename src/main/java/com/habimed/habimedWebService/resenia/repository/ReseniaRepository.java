package com.habimed.habimedWebService.resenia.repository;

import com.habimed.habimedWebService.resenia.domain.model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Integer> {

}
