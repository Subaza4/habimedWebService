package com.habimed.habimedWebService.consultorio.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.dto.*;
import com.habimed.habimedWebService.consultorio.repository.ConsultorioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultorioServiceImpl implements ConsultorioService {

    private final ConsultorioRepository consultorioRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Consultorio> findAll() {
        return consultorioRepository.findAll();
    }

    @Override
    public List<Consultorio> findAllWithConditions(ConsultorioFilterDto consultorioFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Consultorio> consultorios = consultorioRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (consultorioFilterDto.getNombre() != null && !consultorioFilterDto.getNombre().trim().isEmpty()) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getNombre() != null && 
                            c.getNombre().toLowerCase().contains(consultorioFilterDto.getNombre().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (consultorioFilterDto.getUbicacion() != null && !consultorioFilterDto.getUbicacion().trim().isEmpty()) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getUbicacion() != null && 
                            c.getUbicacion().toLowerCase().contains(consultorioFilterDto.getUbicacion().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (consultorioFilterDto.getRuc() != null && !consultorioFilterDto.getRuc().trim().isEmpty()) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getRuc() != null && 
                            c.getRuc().equals(consultorioFilterDto.getRuc()))
                    .collect(Collectors.toList());
        }
        
        return consultorios;
    }

    @Override
    public ConsultorioResponseDto getById(Integer id) {
        Optional<Consultorio> consultorio = consultorioRepository.findById(id);
        if (consultorio.isPresent()) {
            return modelMapper.map(consultorio.get(), ConsultorioResponseDto.class);
        }
        throw new RuntimeException("Consultorio no encontrado con ID: " + id);
    }

    @Override
    public ConsultorioResponseDto save(ConsultorioInsertDto consultorioInsertDto) {
        // Validaciones específicas del contexto de Consultorio
        if (consultorioInsertDto.getRuc() != null && !consultorioInsertDto.getRuc().trim().isEmpty()) {
            // Verificar si ya existe un consultorio con ese RUC
            List<Consultorio> existingConsultorios = consultorioRepository.findAll();
            boolean rucExists = existingConsultorios.stream()
                    .anyMatch(c -> c.getRuc() != null && c.getRuc().equals(consultorioInsertDto.getRuc()));
            
            if (rucExists) {
                throw new RuntimeException("Ya existe un consultorio con RUC: " + consultorioInsertDto.getRuc());
            }
        }
        
        // Verificar si ya existe un consultorio con el mismo nombre en la misma ubicación
        List<Consultorio> existingConsultorios = consultorioRepository.findAll();
        boolean duplicateExists = existingConsultorios.stream()
                .anyMatch(c -> c.getNombre() != null && c.getUbicacion() != null &&
                        c.getNombre().equalsIgnoreCase(consultorioInsertDto.getNombre()) &&
                        c.getUbicacion().equalsIgnoreCase(consultorioInsertDto.getUbicacion()));
        
        if (duplicateExists) {
            throw new RuntimeException("Ya existe un consultorio con el nombre '" + 
                    consultorioInsertDto.getNombre() + "' en la ubicación '" + 
                    consultorioInsertDto.getUbicacion() + "'");
        }
        
        Consultorio consultorio = modelMapper.map(consultorioInsertDto, Consultorio.class);
        Consultorio savedConsultorio = consultorioRepository.save(consultorio);
        return modelMapper.map(savedConsultorio, ConsultorioResponseDto.class);
    }

    @Override
    public ConsultorioResponseDto update(Integer id, ConsultorioUpdateDto consultorioUpdateDto) {
        Optional<Consultorio> existingConsultorio = consultorioRepository.findById(id);
        
        if (existingConsultorio.isPresent()) {
            Consultorio consultorio = existingConsultorio.get();
            
            // Validar RUC único si se está actualizando
            if (consultorioUpdateDto.getRuc() != null && !consultorioUpdateDto.getRuc().trim().isEmpty() &&
                !consultorioUpdateDto.getRuc().equals(consultorio.getRuc())) {
                
                List<Consultorio> existingConsultorios = consultorioRepository.findAll();
                boolean rucExists = existingConsultorios.stream()
                        .anyMatch(c -> c.getIdConsultorio() != consultorio.getIdConsultorio() &&
                                c.getRuc() != null && c.getRuc().equals(consultorioUpdateDto.getRuc()));
                
                if (rucExists) {
                    throw new RuntimeException("Ya existe un consultorio con RUC: " + consultorioUpdateDto.getRuc());
                }
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (consultorioUpdateDto.getRuc() != null && !consultorioUpdateDto.getRuc().trim().isEmpty()) {
                consultorio.setRuc(consultorioUpdateDto.getRuc());
            }
            if (consultorioUpdateDto.getNombre() != null && !consultorioUpdateDto.getNombre().trim().isEmpty()) {
                consultorio.setNombre(consultorioUpdateDto.getNombre());
            }
            if (consultorioUpdateDto.getUbicacion() != null && !consultorioUpdateDto.getUbicacion().trim().isEmpty()) {
                consultorio.setUbicacion(consultorioUpdateDto.getUbicacion());
            }
            if (consultorioUpdateDto.getDireccion() != null && !consultorioUpdateDto.getDireccion().trim().isEmpty()) {
                consultorio.setDireccion(consultorioUpdateDto.getDireccion());
            }
            if (consultorioUpdateDto.getTelefono() != null && !consultorioUpdateDto.getTelefono().trim().isEmpty()) {
                consultorio.setTelefono(consultorioUpdateDto.getTelefono());
            }
            
            Consultorio updatedConsultorio = consultorioRepository.save(consultorio);
            return modelMapper.map(updatedConsultorio, ConsultorioResponseDto.class);
        }
        
        throw new RuntimeException("Consultorio no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Consultorio> consultorio = consultorioRepository.findById(id);
        
        if (consultorio.isPresent()) {
            Consultorio consultorioEntity = consultorio.get();
            
            // Verificar si tiene doctores asociados antes de eliminar
            if (consultorioEntity.getDoctores() != null && !consultorioEntity.getDoctores().isEmpty()) {
                throw new RuntimeException("No se puede eliminar el consultorio con ID " + id + 
                        " porque tiene doctores asociados");
            }
            
            // Verificar si tiene servicios asociados antes de eliminar
            if (consultorioEntity.getServicios() != null && !consultorioEntity.getServicios().isEmpty()) {
                throw new RuntimeException("No se puede eliminar el consultorio con ID " + id + 
                        " porque tiene servicios asociados");
            }
            
            consultorioRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Método específico existente (mantenido para compatibilidad)
    public ConsultorioResponseDto getConsultorioById(Integer id) {
        return getById(id); // Reutilizar el método estándar
    }
}