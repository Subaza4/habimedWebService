package com.habimed.habimedWebService.cita.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.habimedWebService.cita.dto.*;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @Override
    public List<Cita> findAllWithConditions(CitaFilterDto citaFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Cita> citas = citaRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (citaFilterDto.getIdCita() != null) {
            citas = citas.stream()
                    .filter(c -> c.getIdCita().equals(citaFilterDto.getIdCita()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getIdServicio() != null) {
            // Aquí necesitarías acceder al servicio a través de la relación
            // Por ahora filtro comentado hasta tener la relación correcta
             citas = citas.stream()
                     .filter(c -> c.getServicio() != null && c.getServicio().getIdServicio().equals(citaFilterDto.getIdServicio()))
                     .collect(Collectors.toList());
        }
        
         if (citaFilterDto.getIdConsultorio() != null) {
            // Similar al servicio, necesitarías la relación con consultorio
             citas = citas.stream()
                     .filter(c -> c.getDoctor().getConsultorio() != null && c.getDoctor().getConsultorio().getIdConsultorio().equals(citaFilterDto.getIdConsultorio()))
                     .collect(Collectors.toList());
         }
        
        if (citaFilterDto.getIdMedico() != null) {
            citas = citas.stream()
                    .filter(c -> c.getDoctor() != null && c.getDoctor().getIdUsuario().equals(citaFilterDto.getIdMedico()))
                    .collect(Collectors.toList());
        }

        if (citaFilterDto.getDniPersona() != null && citaFilterDto.getDniPersona() > 0) {
            citas = citas.stream()
                    .filter(c -> c.getPaciente() != null &&
                            c.getPaciente().getPersona() != null &&
                            c.getPaciente().getPersona().getDni().equals(citaFilterDto.getDniPersona()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getMotivo() != null && !citaFilterDto.getMotivo().trim().isEmpty()) {
            citas = citas.stream()
                    .filter(c -> c.getMotivo() != null && 
                            c.getMotivo().toLowerCase().contains(citaFilterDto.getMotivo().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getFechaHoraInicio() != null) {
            citas = citas.stream()
                    .filter(c -> c.getFechaHoraInicio() != null && 
                            c.getFechaHoraInicio().toLocalDate().equals(citaFilterDto.getFechaHoraInicio().toLocalDate()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getFechaHoraFin() != null) {
            citas = citas.stream()
                    .filter(c -> c.getFechaHoraFin() != null && 
                            c.getFechaHoraFin().toLocalDate().equals(citaFilterDto.getFechaHoraFin().toLocalDate()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getEstado() != null) {
            citas = citas.stream()
                    .filter(c -> c.getEstado() != null && c.getEstado().equals(citaFilterDto.getEstado()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getDescripcion() != null && !citaFilterDto.getDescripcion().trim().isEmpty()) {
            citas = citas.stream()
                    .filter(c -> c.getDescripcion() != null && 
                            c.getDescripcion().toLowerCase().contains(citaFilterDto.getDescripcion().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return citas;
    }

    @Override
    public CitaResponseDto getById(Integer id) {
        Optional<Cita> cita = citaRepository.findById(id);
        if (cita.isPresent()) {
            return modelMapper.map(cita.get(), CitaResponseDto.class);
        }
        throw new RuntimeException("Cita no encontrada con ID: " + id);
    }

    @Override
    public CitaResponseDto save(CitaInsertDto citaInsertDto) {
        // Validaciones de negocio antes de guardar
        if (citaInsertDto.getFechaHoraInicio() != null && citaInsertDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede crear una cita con fecha pasada");
        }
        
        if (citaInsertDto.getFechaHoraInicio() != null && citaInsertDto.getFechaHoraFin() != null &&
            citaInsertDto.getFechaHoraFin().isBefore(citaInsertDto.getFechaHoraInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        Cita cita = modelMapper.map(citaInsertDto, Cita.class);
        
        // Establecer estado por defecto si no se proporciona
        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCitaEnum.PROGRAMADA);
        }
        
        Cita savedCita = citaRepository.save(cita);
        return modelMapper.map(savedCita, CitaResponseDto.class);
    }

    @Override
    public CitaResponseDto update(Integer id, CitaUpdateDto citaUpdateDto) {
        Optional<Cita> existingCita = citaRepository.findById(id);
        
        if (existingCita.isPresent()) {
            Cita cita = existingCita.get();
            
            // Actualizar solo los campos que no son null en el DTO
            if (citaUpdateDto.getMotivo() != null && !citaUpdateDto.getMotivo().trim().isEmpty()) {
                cita.setMotivo(citaUpdateDto.getMotivo());
            }
            if (citaUpdateDto.getFechaHoraInicio() != null) {
                // Validar que no sea una fecha pasada
                if (citaUpdateDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("No se puede actualizar a una fecha pasada");
                }
                cita.setFechaHoraInicio(citaUpdateDto.getFechaHoraInicio());
            }
            if (citaUpdateDto.getFechaHoraFin() != null) {
                cita.setFechaHoraFin(citaUpdateDto.getFechaHoraFin());
            }
            if (citaUpdateDto.getEstado() != null) {
                cita.setEstado(citaUpdateDto.getEstado());
            }
            if (citaUpdateDto.getDescripcion() != null) {
                cita.setDescripcion(citaUpdateDto.getDescripcion());
            }
            
            // Validar consistencia de fechas después de las actualizaciones
            if (cita.getFechaHoraFin() != null && cita.getFechaHoraInicio() != null &&
                cita.getFechaHoraFin().isBefore(cita.getFechaHoraInicio())) {
                throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
            }
            
            Cita updatedCita = citaRepository.save(cita);
            return modelMapper.map(updatedCita, CitaResponseDto.class);
        }
        
        throw new RuntimeException("Cita no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Cita> cita = citaRepository.findById(id);
        
        if (cita.isPresent()) {
            Cita citaEntity = cita.get();
            
            // Validar si la cita puede ser eliminada según su estado
            if (citaEntity.getEstado() == EstadoCitaEnum.COMPLETADA) {
                throw new RuntimeException("No se puede eliminar una cita que ya fue completada");
            }
            
            // Verificar si tiene relaciones dependientes
            if (citaEntity.getDiagnosticos() != null && !citaEntity.getDiagnosticos().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la cita porque tiene diagnósticos asociados");
            }
            
            if (citaEntity.getRecetas() != null && !citaEntity.getRecetas().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la cita porque tiene recetas asociadas");
            }
            
            if (citaEntity.getRecomendaciones() != null && !citaEntity.getRecomendaciones().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la cita porque tiene recomendaciones asociadas");
            }
            
            if (citaEntity.getDetallePago() != null) {
                throw new RuntimeException("No se puede eliminar la cita porque tiene un pago asociado");
            }
            
            citaRepository.deleteById(id);
            return true;
        }
        
        return false;
    }
}