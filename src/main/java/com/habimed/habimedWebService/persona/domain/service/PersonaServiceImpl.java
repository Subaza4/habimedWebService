package com.habimed.habimedWebService.persona.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.*;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    @Override
    public List<Persona> findAllWithConditions(PersonaFilterDto personaFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Persona> personas = personaRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (personaFilterDto.getDni() != null) {
            personas = personas.stream()
                    .filter(p -> p.getDni().equals(personaFilterDto.getDni()))
                    .collect(Collectors.toList());
        }
        
        if (personaFilterDto.getNombres() != null && !personaFilterDto.getNombres().trim().isEmpty()) {
            String nombresBuscados = personaFilterDto.getNombres().toLowerCase().trim();
            personas = personas.stream()
                    .filter(p -> p.getNombres() != null && 
                            p.getNombres().toLowerCase().contains(nombresBuscados))
                    .collect(Collectors.toList());
        }
        
        if (personaFilterDto.getApellidos() != null && !personaFilterDto.getApellidos().trim().isEmpty()) {
            String apellidosBuscados = personaFilterDto.getApellidos().toLowerCase().trim();
            personas = personas.stream()
                    .filter(p -> p.getApellidos() != null && 
                            p.getApellidos().toLowerCase().contains(apellidosBuscados))
                    .collect(Collectors.toList());
        }
        
        /*if (personaFilterDto.getCorreo() != null && !personaFilterDto.getCorreo().trim().isEmpty()) {
            String correoBuscado = personaFilterDto.getCorreo().toLowerCase().trim();
            personas = personas.stream()
                    .filter(p -> p.getCorreo() != null && 
                            p.getCorreo().toLowerCase().contains(correoBuscado))
                    .collect(Collectors.toList());
        }*/
        
        if (personaFilterDto.getFechaNacimiento() != null) {
            personas = personas.stream()
                    .filter(p -> p.getFechaNacimiento() != null && 
                            p.getFechaNacimiento().equals(personaFilterDto.getFechaNacimiento()))
                    .collect(Collectors.toList());
        }
        
        return personas;
    }

    @Override
    public PersonaResponseDto getById(Long dni) {
        Optional<Persona> persona = personaRepository.findById(dni);
        if (persona.isPresent()) {
            return mapToResponseDto(persona.get());
        }
        throw new RuntimeException("Persona no encontrada con DNI: " + dni);
    }

    @Override
    public PersonaResponseDto save(PersonaInsertDto personaInsertDto) {
        // Validaciones específicas del contexto de Persona
        if (personaInsertDto.getDni() == null) {
            throw new RuntimeException("El DNI es obligatorio");
        }
        
        // Validar formato de DNI (8 dígitos)
        if (!validarDNI(personaInsertDto.getDni())) {
            throw new RuntimeException("El DNI debe tener exactamente 8 dígitos");
        }
        
        // Verificar si ya existe una persona con ese DNI
        if (personaRepository.existsById(personaInsertDto.getDni())) {
            throw new RuntimeException("Ya existe una persona registrada con DNI: " + personaInsertDto.getDni());
        }
        
        // Validar edad mínima (debe ser mayor de edad)
        if (personaInsertDto.getFechaNacimiento() != null) {
            if (!validarEdadMinima(personaInsertDto.getFechaNacimiento())) {
                throw new RuntimeException("La persona debe ser mayor de edad (18 años)");
            }
            
            // Validar que la fecha de nacimiento no sea futura
            if (personaInsertDto.getFechaNacimiento().isAfter(LocalDate.now())) {
                throw new RuntimeException("La fecha de nacimiento no puede ser futura");
            }
            
            // Validar edad máxima razonable (150 años)
            if (personaInsertDto.getFechaNacimiento().isBefore(LocalDate.now().minusYears(150))) {
                throw new RuntimeException("La fecha de nacimiento no puede ser mayor a 150 años");
            }
        }
        
        // Validar formato de celular
        if (personaInsertDto.getCelular() != null && !personaInsertDto.getCelular().trim().isEmpty()) {
            if (!validarCelular(personaInsertDto.getCelular())) {
                throw new RuntimeException("El número de celular debe tener 9 dígitos y comenzar con 9");
            }
        }
        
        // Validar nombres y apellidos (solo letras y espacios)
        if (!validarNombreApellido(personaInsertDto.getNombres())) {
            throw new RuntimeException("Los nombres solo pueden contener letras y espacios");
        }
        
        if (!validarNombreApellido(personaInsertDto.getApellidos())) {
            throw new RuntimeException("Los apellidos solo pueden contener letras y espacios");
        }
        
        // Normalizar nombres y apellidos
        personaInsertDto.setNombres(normalizarTexto(personaInsertDto.getNombres()));
        personaInsertDto.setApellidos(normalizarTexto(personaInsertDto.getApellidos()));
        
        if (personaInsertDto.getDireccion() != null) {
            personaInsertDto.setDireccion(normalizarTexto(personaInsertDto.getDireccion()));
        }
        
        Persona persona = modelMapper.map(personaInsertDto, Persona.class);
        Persona savedPersona = personaRepository.save(persona);
        return mapToResponseDto(savedPersona);
    }

    @Override
    public PersonaResponseDto update(Long dni, PersonaUpdateDto personaUpdateDto) {
        Optional<Persona> existingPersona = personaRepository.findById(dni);
        
        if (existingPersona.isPresent()) {
            Persona persona = existingPersona.get();
            
            // Verificar si la persona tiene usuarios asociados antes de permitir ciertos cambios
            if (persona.getUsuarios() != null && !persona.getUsuarios().isEmpty()) {
                // Si tiene usuarios asociados, ser más restrictivo con los cambios
                if (personaUpdateDto.getFechaNacimiento() != null && 
                    !personaUpdateDto.getFechaNacimiento().equals(persona.getFechaNacimiento())) {
                    throw new RuntimeException("No se puede cambiar la fecha de nacimiento de una persona con usuarios asociados");
                }
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (personaUpdateDto.getNombres() != null && !personaUpdateDto.getNombres().trim().isEmpty()) {
                if (!validarNombreApellido(personaUpdateDto.getNombres())) {
                    throw new RuntimeException("Los nombres solo pueden contener letras y espacios");
                }
                persona.setNombres(normalizarTexto(personaUpdateDto.getNombres()));
            }
            
            if (personaUpdateDto.getApellidos() != null && !personaUpdateDto.getApellidos().trim().isEmpty()) {
                if (!validarNombreApellido(personaUpdateDto.getApellidos())) {
                    throw new RuntimeException("Los apellidos solo pueden contener letras y espacios");
                }
                persona.setApellidos(normalizarTexto(personaUpdateDto.getApellidos()));
            }
            
            /*if (personaUpdateDto.getCorreo() != null && !personaUpdateDto.getCorreo().trim().isEmpty()) {
                if (!validarEmail(personaUpdateDto.getCorreo())) {
                    throw new RuntimeException("El formato del correo electrónico no es válido");
                }
                persona.setCorreo(personaUpdateDto.getCorreo().toLowerCase().trim());
            }*/
            
            if (personaUpdateDto.getCelular() != null && !personaUpdateDto.getCelular().trim().isEmpty()) {
                if (!validarCelular(personaUpdateDto.getCelular())) {
                    throw new RuntimeException("El número de celular debe tener 9 dígitos y comenzar con 9");
                }
                persona.setCelular(personaUpdateDto.getCelular());
            }
            
            if (personaUpdateDto.getDireccion() != null && !personaUpdateDto.getDireccion().trim().isEmpty()) {
                persona.setDireccion(normalizarTexto(personaUpdateDto.getDireccion()));
            }
            
            if (personaUpdateDto.getFechaNacimiento() != null) {
                // Validar la nueva fecha de nacimiento
                if (!validarEdadMinima(personaUpdateDto.getFechaNacimiento())) {
                    throw new RuntimeException("La persona debe ser mayor de edad (18 años)");
                }
                
                if (personaUpdateDto.getFechaNacimiento().isAfter(LocalDate.now())) {
                    throw new RuntimeException("La fecha de nacimiento no puede ser futura");
                }
                
                if (personaUpdateDto.getFechaNacimiento().isBefore(LocalDate.now().minusYears(150))) {
                    throw new RuntimeException("La fecha de nacimiento no puede ser mayor a 150 años");
                }
                
                persona.setFechaNacimiento(personaUpdateDto.getFechaNacimiento());
            }
            
            Persona updatedPersona = personaRepository.save(persona);
            return mapToResponseDto(updatedPersona);
        }
        
        throw new RuntimeException("Persona no encontrada con DNI: " + dni);
    }

    @Override
    public Boolean delete(Long dni) {
        if (personaRepository.existsById(dni)) {
            Optional<Persona> persona = personaRepository.findById(dni);
            
            if (persona.isPresent()) {
                Persona personaEntity = persona.get();
                
                // Verificar si la persona tiene usuarios asociados antes de eliminar
                if (personaEntity.getUsuarios() != null && !personaEntity.getUsuarios().isEmpty()) {
                    throw new RuntimeException("No se puede eliminar la persona con DNI " + dni + 
                            " porque tiene usuarios asociados. Primero debe eliminar o reasignar los usuarios.");
                }
                
                // Verificar si tiene citas como paciente
                boolean tieneCitasComoPaciente = personaEntity.getUsuarios().stream()
                        .anyMatch(usuario -> usuario.getCitasComoPaciente() != null && 
                                !usuario.getCitasComoPaciente().isEmpty());
                
                if (tieneCitasComoPaciente) {
                    throw new RuntimeException("No se puede eliminar la persona porque tiene citas médicas asociadas como paciente");
                }
                
                // Verificar si tiene citas como doctor
                boolean tieneCitasComoDoctor = personaEntity.getUsuarios().stream()
                        .anyMatch(usuario -> usuario.getCitasComoDoctor() != null && 
                                !usuario.getCitasComoDoctor().isEmpty());
                
                if (tieneCitasComoDoctor) {
                    throw new RuntimeException("No se puede eliminar la persona porque tiene citas médicas asociadas como doctor");
                }
                
                personaRepository.deleteById(dni);
                return true;
            }
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de Persona
    private boolean validarDNI(Long dni) {
        if (dni == null) return false;
        String dniStr = dni.toString();
        return dniStr.length() == 8 && dniStr.matches("\\d{8}");
    }
    
    private boolean validarEdadMinima(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return true;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }
    
    private boolean validarCelular(String celular) {
        if (celular == null || celular.trim().isEmpty()) return true;
        String celularClean = celular.trim();
        return celularClean.matches("^9\\d{8}$"); // 9 dígitos que comienzan con 9
    }
    
    private boolean validarNombreApellido(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        return texto.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }
    
    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return true;
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
    
    private String normalizarTexto(String texto) {
        if (texto == null) return null;
        return texto.trim().replaceAll("\\s+", " ");
    }
    
    public int calcularEdad(Long dni) {
        Optional<Persona> persona = personaRepository.findById(dni);
        if (persona.isPresent() && persona.get().getFechaNacimiento() != null) {
            return Period.between(persona.get().getFechaNacimiento(), LocalDate.now()).getYears();
        }
        throw new RuntimeException("No se puede calcular la edad para la persona con DNI: " + dni);
    }
    
    public List<Persona> findByRangoEdad(int edadMinima, int edadMaxima) {
        List<Persona> todasPersonas = personaRepository.findAll();
        LocalDate fechaMaxima = LocalDate.now().minusYears(edadMinima);
        LocalDate fechaMinima = LocalDate.now().minusYears(edadMaxima + 1);
        
        return todasPersonas.stream()
                .filter(p -> p.getFechaNacimiento() != null)
                .filter(p -> !p.getFechaNacimiento().isAfter(fechaMaxima) && 
                           p.getFechaNacimiento().isAfter(fechaMinima))
                .collect(Collectors.toList());
    }
    
    public List<Persona> findByNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String nombreNormalizado = nombreCompleto.toLowerCase().trim();
        return personaRepository.findAll().stream()
                .filter(p -> {
                    String nombreCompletoPersona = (p.getNombres() + " " + p.getApellidos()).toLowerCase();
                    return nombreCompletoPersona.contains(nombreNormalizado);
                })
                .collect(Collectors.toList());
    }

    // Método helper para mapear a ResponseDto con información adicional
    private PersonaResponseDto mapToResponseDto(Persona persona) {
        PersonaResponseDto responseDto = modelMapper.map(persona, PersonaResponseDto.class);
        
        // Agregar información calculada
        /*if (persona.getFechaNacimiento() != null) {
            int edad = Period.between(persona.getFechaNacimiento(), LocalDate.now()).getYears();
            responseDto.setEdad(edad);
        }*/
        
        // Agregar información de usuarios asociados si los tiene
        /*if (persona.getUsuarios() != null && !persona.getUsuarios().isEmpty()) {
            responseDto.setTieneUsuarios(true);
            responseDto.setCantidadUsuarios(persona.getUsuarios().size());
            
            // Agregar tipos de usuario asociados
            List<String> tiposUsuario = persona.getUsuarios().stream()
                    .map(usuario -> usuario.getTipoUsuario().name())
                    .distinct()
                    .collect(Collectors.toList());
            responseDto.setTiposUsuario(tiposUsuario);
        } else {
            responseDto.setTieneUsuarios(false);
            responseDto.setCantidadUsuarios(0);
        }

        // Formatear nombre completo
        responseDto.setNombreCompleto(persona.getNombres() + " " + persona.getApellidos());
        */
        return responseDto;
    }
}