package com.habimed.habimedWebService.diagnostico.repository;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Integer> {

}
