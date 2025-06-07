package com.habimed.habimedWebService.persona.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.PersonaRequest;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public List<Persona> getAllPersonas(PersonaRequest request) {
        return personaRepository.findAll(request);
    }

    @Override
    public Optional<Persona> getPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    @Override
    public int setPersona(PersonaRequest persona) {
        return personaRepository.setPersona(persona);
    }

    @Override
    public boolean deletePersona(Long id) {
        return personaRepository.deleteById(id);
    }
}