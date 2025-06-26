package com.habimed.habimedWebService.persona.domain.service;

import java.util.List;
import java.util.Optional;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.PersonaFilterDto;
import com.habimed.habimedWebService.persona.dto.PersonaInsertDto;
import com.habimed.habimedWebService.persona.dto.PersonaRequest;
import com.habimed.habimedWebService.persona.dto.PersonaUpdateDto;


public interface PersonaService {

    /**
     * Retrieves all persons
     * @return List of all persons
     */
    List<Persona> findAll(PersonaFilterDto request);

    /**
     * Retrieves a person by its ID
     * @param dni The person ID
     * @return Optional containing the person if found, empty otherwise
     */
    Optional<Persona> findById(Long dni);

    /**
     * Saves a new person
     * @param persona The person to save
     * @return The saved person with generated ID
     */
    Persona savePersona(PersonaInsertDto persona);

    /**
     * update a exist persona
     * @param persona
     * @return
     */
    Persona updatePersona(PersonaUpdateDto persona, Long dni);

    /**
     * Deletes a product by its ID
     * @param dni The product ID to delete
     * @return true if deleted successfully, false otherwise
     */
    Boolean deletePersona(Long dni);
}
