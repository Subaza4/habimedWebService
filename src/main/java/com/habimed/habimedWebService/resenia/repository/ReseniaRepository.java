package com.habimed.habimedWebService.resenia.repository;

import com.habimed.habimedWebService.resenia.domain.model.Resenia;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReseniaRepository extends JpaRepository<Resenia, Integer> {

}
