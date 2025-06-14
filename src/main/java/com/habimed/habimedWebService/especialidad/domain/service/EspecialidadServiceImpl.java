package com.habimed.habimedWebService.especialidad.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.*;
import com.habimed.habimedWebService.especialidad.repository.EspecialidadRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    @Override
    public List<Especialidad> findAllWithConditions(EspecialidadFilterDto especialidadFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Especialidad> especialidades = especialidadRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (especialidadFilterDto.getIdEspecialidad() != null) {
            especialidades = especialidades.stream()
                    .filter(e -> e.getIdEspecialidad().equals(especialidadFilterDto.getIdEspecialidad()))
                    .collect(Collectors.toList());
        }
        
        if (especialidadFilterDto.getNombre() != null && !especialidadFilterDto.getNombre().trim().isEmpty()) {
            especialidades = especialidades.stream()
                    .filter(e -> e.getNombre() != null && 
                            e.getNombre().toLowerCase().contains(especialidadFilterDto.getNombre().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (especialidadFilterDto.getDescripcionContiene() != null && 
            !especialidadFilterDto.getDescripcionContiene().trim().isEmpty()) {
            especialidades = especialidades.stream()
                    .filter(e -> e.getDescripcion() != null && 
                            e.getDescripcion().toLowerCase().contains(
                                    especialidadFilterDto.getDescripcionContiene().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return especialidades;
    }

    @Override
    public EspecialidadResponseDto getById(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        if (especialidad.isPresent()) {
            return mapToResponseDto(especialidad.get());
        }
        throw new RuntimeException("Especialidad no encontrada con ID: " + id);
    }

    @Override
    public EspecialidadResponseDto save(EspecialidadInsertDto especialidadInsertDto) {
        // Validaciones específicas del contexto de Especialidad
        if (especialidadInsertDto.getNombre() == null || especialidadInsertDto.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la especialidad es obligatorio");
        }
        
        // Verificar que no exista ya una especialidad con el mismo nombre (case insensitive)
        List<Especialidad> existingEspecialidades = especialidadRepository.findAll();
        boolean nombreExists = existingEspecialidades.stream()
                .anyMatch(e -> e.getNombre() != null && 
                        e.getNombre().equalsIgnoreCase(especialidadInsertDto.getNombre().trim()));
        
        if (nombreExists) {
            throw new RuntimeException("Ya existe una especialidad con el nombre: " + 
                    especialidadInsertDto.getNombre());
        }
        
        // Normalizar el nombre (capitalizar primera letra de cada palabra)
        String nombreNormalizado = capitalizarNombre(especialidadInsertDto.getNombre().trim());
        
        Especialidad especialidad = modelMapper.map(especialidadInsertDto, Especialidad.class);
        especialidad.setNombre(nombreNormalizado);
        
        // Normalizar descripción si existe
        if (especialidad.getDescripcion() != null && !especialidad.getDescripcion().trim().isEmpty()) {
            especialidad.setDescripcion(especialidad.getDescripcion().trim());
        }
        
        Especialidad savedEspecialidad = especialidadRepository.save(especialidad);
        return mapToResponseDto(savedEspecialidad);
    }

    @Override
    public EspecialidadResponseDto update(Integer id, EspecialidadUpdateDto especialidadUpdateDto) {
        Optional<Especialidad> existingEspecialidad = especialidadRepository.findById(id);
        
        if (existingEspecialidad.isPresent()) {
            Especialidad especialidad = existingEspecialidad.get();
            
            // Validar nombre único si se está actualizando
            if (especialidadUpdateDto.getNombre() != null && 
                !especialidadUpdateDto.getNombre().trim().isEmpty() &&
                !especialidadUpdateDto.getNombre().equalsIgnoreCase(especialidad.getNombre())) {
                
                List<Especialidad> existingEspecialidades = especialidadRepository.findAll();
                boolean nombreExists = existingEspecialidades.stream()
                        .anyMatch(e -> e.getIdEspecialidad() != especialidad.getIdEspecialidad() &&
                                e.getNombre() != null && 
                                e.getNombre().equalsIgnoreCase(especialidadUpdateDto.getNombre().trim()));
                
                if (nombreExists) {
                    throw new RuntimeException("Ya existe una especialidad con el nombre: " + 
                            especialidadUpdateDto.getNombre());
                }
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (especialidadUpdateDto.getNombre() != null && !especialidadUpdateDto.getNombre().trim().isEmpty()) {
                String nombreNormalizado = capitalizarNombre(especialidadUpdateDto.getNombre().trim());
                especialidad.setNombre(nombreNormalizado);
            }
            
            if (especialidadUpdateDto.getDescripcion() != null) {
                if (especialidadUpdateDto.getDescripcion().trim().isEmpty()) {
                    especialidad.setDescripcion(null); // Permitir limpiar la descripción
                } else {
                    especialidad.setDescripcion(especialidadUpdateDto.getDescripcion().trim());
                }
            }
            
            Especialidad updatedEspecialidad = especialidadRepository.save(especialidad);
            return mapToResponseDto(updatedEspecialidad);
        }
        
        throw new RuntimeException("Especialidad no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        
        if (especialidad.isPresent()) {
            Especialidad especialidadEntity = especialidad.get();
            
            // Verificar si tiene servicios asociados antes de eliminar
            if (especialidadEntity.getServicios() != null && !especialidadEntity.getServicios().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la especialidad '" + 
                        especialidadEntity.getNombre() + "' porque tiene servicios asociados");
            }
            
            especialidadRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Método helper para mapear a ResponseDto con información adicional
    private EspecialidadResponseDto mapToResponseDto(Especialidad especialidad) {
        EspecialidadResponseDto responseDto = modelMapper.map(especialidad, EspecialidadResponseDto.class);
        
        // Agregar información adicional como la cantidad de servicios
        if (especialidad.getServicios() != null) {
            responseDto.setCantidadServicios(especialidad.getServicios().size());
        } else {
            responseDto.setCantidadServicios(0);
        }
        
        return responseDto;
    }

    // Método helper para capitalizar nombres
    private String capitalizarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return nombre;
        }
        
        String[] palabras = nombre.toLowerCase().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < palabras.length; i++) {
            if (i > 0) {
                resultado.append(" ");
            }
            if (!palabras[i].isEmpty()) {
                resultado.append(Character.toUpperCase(palabras[i].charAt(0)))
                         .append(palabras[i].substring(1));
            }
        }
        
        return resultado.toString();
    }
}