package com.habimed.habimedWebService.diagnostico.repository;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiagnosticoRepository extends JpaRepository<Diagnostico, Integer> {

}
