package com.habimed.habimedWebService.persona.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.habimed.habimedWebService.persona.domain.model.Persona;

@Repository
@RequiredArgsConstructor
public class PersonaRepository extends JpaRepository<Persona, Integer> {

}