package com.habimed.habimedWebService.persona.domain.service;

import java.beans.Beans;
import java.util.List;
import java.util.Optional;

import com.habimed.habimedWebService.persona.dto.PersonaFilterDto;
import com.habimed.habimedWebService.persona.dto.PersonaInsertDto;
import com.habimed.habimedWebService.persona.dto.PersonaUpdateDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public List<Persona> findAll(PersonaFilterDto request) {
        return personaRepository.findAll(request);
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }

    @Override
    public Persona savePersona(PersonaInsertDto persona) {
        //verificar que no exista el registro
        Optional<Persona> _personaFind = personaRepository.findById(persona.getDni());
        if(!_personaFind.isPresent()){
            Persona _persona = new Persona();
            BeanUtils.copyProperties(persona, _persona);

            Integer result = personaRepository.setPersona(_persona);
            if(result == 1){
                //devolver el objeto persona
                Optional<Persona> _personaRes = personaRepository.findById(persona.getDni());
                return _personaRes.get();
            }else{
                throw new RuntimeException("No se pudo registrar la persona.");
            }
        }else{
            throw new RuntimeException("El DNI ya existe en la base de datos: " + persona.getDni() + ".");
        }
    }

    @Override
    public Persona updatePersona(PersonaUpdateDto persona, Long dni) {
        //verificar que no exista el registro
        Optional<Persona> _personaFind = personaRepository.findById(dni);

        if(_personaFind.isPresent()){
            Persona _persona = new Persona();
            BeanUtils.copyProperties(persona, _persona);
            _persona.setDni(dni);

            Integer result = personaRepository.setPersona(_persona);
            if(result == 2){
                //devolver el objeto persona
                Optional<Persona> _personaRes = personaRepository.findById(dni);
                return _personaRes.get();
            }else{
                throw new RuntimeException("No se pudo actualizar la persona.");
            }
        }else{
            throw new RuntimeException("No se encotró a la persona con el DNI: " + dni + ".");
        }
    }

    @Override
    public Boolean deletePersona(Long dni) {
        return personaRepository.deleteById(dni);
    }
}