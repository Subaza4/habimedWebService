package com.habimed.habimedWebService.persona.application;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.service.PersonaService;

import com.habimed.habimedWebService.persona.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Para devolver la respuesta HTTP completa
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonasController {

    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        try {
            List<Persona> personas = personaService.findAll();
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Persona>> getPersonasWithFilter(@Valid @RequestBody PersonaFilterDto filterDto) {
        try {
            List<Persona> personas = personaService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{dni}")
    public ResponseEntity<PersonaResponseDto> getPersonaByDni(@PathVariable Long dni) {
        try {
            PersonaResponseDto persona = personaService.getById(dni);
            if (persona != null) {
                return ResponseEntity.ok(persona);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PersonaResponseDto> createPersona(@Valid @RequestBody PersonaInsertDto personaInsertDto) {
        try {
            PersonaResponseDto createdPersona = personaService.save(personaInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersona);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{dni}")
    public ResponseEntity<PersonaResponseDto> updatePersona(
            @PathVariable Long dni,
            @Valid @RequestBody PersonaUpdateDto personaUpdateDto) {
        try {
            PersonaResponseDto updatedPersona = personaService.update(dni, personaUpdateDto);
            if (updatedPersona != null) {
                return ResponseEntity.ok(updatedPersona);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long dni) {
        try {
            Boolean deleted = personaService.delete(dni);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}