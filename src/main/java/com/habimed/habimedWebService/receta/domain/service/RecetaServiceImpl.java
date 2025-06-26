package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.receta.dto.RecetaFilterDto;
import com.habimed.habimedWebService.receta.dto.RecetaInsertDto;
import com.habimed.habimedWebService.receta.dto.RecetaResponseDto;
import com.habimed.habimedWebService.receta.dto.RecetaUpdateDto;
import com.habimed.habimedWebService.receta.repository.RecetaRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {
    
    private final RecetaRepository recetaRepository;
    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Receta> findAll() {
        return recetaRepository.findAll();
    }

    @Override
    public List<Receta> findAllWithConditions(RecetaFilterDto recetaFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Receta> recetas = recetaRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (recetaFilterDto.getIdCita() != null) {
            recetas = recetas.stream()
                    .filter(r -> r.getCita() != null && 
                            r.getCita().getIdCita().equals(recetaFilterDto.getIdCita()))
                    .collect(Collectors.toList());
        }
        
        if (recetaFilterDto.getDescripcionContiene() != null && 
            !recetaFilterDto.getDescripcionContiene().trim().isEmpty()) {
            String descripcionBuscada = recetaFilterDto.getDescripcionContiene().toLowerCase().trim();
            recetas = recetas.stream()
                    .filter(r -> r.getDescripcion() != null && 
                            r.getDescripcion().toLowerCase().contains(descripcionBuscada))
                    .collect(Collectors.toList());
        }
        
        if (recetaFilterDto.getFechaRecetaInicio() != null) {
            recetas = recetas.stream()
                    .filter(r -> r.getFechaReceta() != null && 
                            !r.getFechaReceta().isBefore(recetaFilterDto.getFechaRecetaInicio()))
                    .collect(Collectors.toList());
        }
        
        if (recetaFilterDto.getFechaRecetaFin() != null) {
            recetas = recetas.stream()
                    .filter(r -> r.getFechaReceta() != null && 
                            !r.getFechaReceta().isAfter(recetaFilterDto.getFechaRecetaFin()))
                    .collect(Collectors.toList());
        }
        
        return recetas;
    }

    @Override
    public RecetaResponseDto getById(Integer id) {
        Optional<Receta> receta = recetaRepository.findById(id);
        if (receta.isPresent()) {
            return mapToResponseDto(receta.get());
        }
        throw new RuntimeException("Receta no encontrada con ID: " + id);
    }

    @Override
    public RecetaResponseDto save(RecetaInsertDto recetaInsertDto) {
        // Validaciones específicas del contexto de Receta
        if (recetaInsertDto.getIdCita() == null) {
            throw new RuntimeException("El ID de la cita es obligatorio para crear una receta");
        }
        
        // Verificar que la cita existe
        Optional<Cita> cita = citaRepository.findById(recetaInsertDto.getIdCita());
        if (!cita.isPresent()) {
            throw new RuntimeException("No existe una cita con ID: " + recetaInsertDto.getIdCita());
        }
        
        Cita citaEntity = cita.get();
        
        // Verificar que la cita esté completada o en proceso (no se pueden crear recetas para citas futuras o canceladas)
        if (citaEntity.getEstado() == EstadoCitaEnum.CANCELADA) {
            throw new RuntimeException("No se puede crear una receta para una cita cancelada");
        }
        
        if (citaEntity.getEstado() == EstadoCitaEnum.PROGRAMADA && 
            citaEntity.getFechaHoraInicio() != null && 
            citaEntity.getFechaHoraInicio().toLocalDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("No se puede crear una receta para una cita que aún no ha ocurrido");
        }
        
        // Verificar que el doctor esté asignado a la cita
        if (citaEntity.getDoctor() == null) {
            throw new RuntimeException("No se puede crear una receta para una cita sin doctor asignado");
        }
        
        // Validar que el doctor sea realmente un doctor
        if (citaEntity.getDoctor().getTipoUsuario() != TipoUsuarioEnum.DOCTOR) {
            throw new RuntimeException("Solo un doctor puede crear recetas médicas");
        }
        
        // Verificar si ya existe una receta para esta cita (opcional - podría permitirse múltiples recetas)
        List<Receta> recetasExistentes = recetaRepository.findAll();
        boolean recetaExists = recetasExistentes.stream()
                .anyMatch(r -> r.getCita() != null && 
                        r.getCita().getIdCita().equals(recetaInsertDto.getIdCita()));
        
        if (recetaExists) {
            // Advertencia en lugar de error - permitir múltiples recetas pero notificar
            System.out.println("ADVERTENCIA: Ya existe una receta para la cita ID: " + recetaInsertDto.getIdCita());
        }
        
        // Validar contenido de la descripción
        if (!validarDescripcionReceta(recetaInsertDto.getDescripcion())) {
            throw new RuntimeException("La descripción de la receta contiene información no válida o está vacía");
        }
        
        // Verificar longitud mínima de descripción
        if (recetaInsertDto.getDescripcion().trim().length() < 10) {
            throw new RuntimeException("La descripción de la receta debe tener al menos 10 caracteres");
        }
        
        Receta receta = modelMapper.map(recetaInsertDto, Receta.class);
        receta.setCita(citaEntity);
        
        // Establecer fecha actual si no se proporciona
        if (receta.getFechaReceta() == null) {
            receta.setFechaReceta(LocalDate.now());
        }
        
        // Validar que la fecha de la receta no sea anterior a la fecha de la cita
        if (citaEntity.getFechaHoraInicio() != null && 
            receta.getFechaReceta().isBefore(citaEntity.getFechaHoraInicio().toLocalDate())) {
            throw new RuntimeException("La fecha de la receta no puede ser anterior a la fecha de la cita");
        }
        
        // Validar que la fecha de la receta no sea futura (más de 1 día)
        if (receta.getFechaReceta().isAfter(LocalDate.now().plusDays(1))) {
            throw new RuntimeException("La fecha de la receta no puede ser más de 1 día en el futuro");
        }
        
        // Normalizar y limpiar la descripción
        receta.setDescripcion(normalizarDescripcion(receta.getDescripcion()));
        
        Receta savedReceta = recetaRepository.save(receta);
        return mapToResponseDto(savedReceta);
    }

    @Override
    public RecetaResponseDto update(Integer id, RecetaUpdateDto recetaUpdateDto) {
        Optional<Receta> existingReceta = recetaRepository.findById(id);
        
        if (existingReceta.isPresent()) {
            Receta receta = existingReceta.get();
            
            // Verificar que la receta no sea muy antigua (regla de negocio para modificaciones)
            if (receta.getFechaReceta() != null && 
                receta.getFechaReceta().isBefore(LocalDate.now().minusDays(7))) {
                throw new RuntimeException("No se puede modificar una receta con más de 7 días de antigüedad");
            }
            
            // Verificar que la cita asociada no esté completada hace mucho tiempo
            if (receta.getCita() != null && receta.getCita().getEstado() == EstadoCitaEnum.COMPLETADA &&
                receta.getCita().getFechaHoraFin() != null &&
                receta.getCita().getFechaHoraFin().toLocalDate().isBefore(LocalDate.now().minusDays(30))) {
                throw new RuntimeException("No se puede modificar una receta de una cita completada hace más de 30 días");
            }
            
            // Validar permisos - solo el doctor que creó la cita debería poder modificar la receta
            // Esta validación dependería del contexto de seguridad actual
            
            // Actualizar solo los campos que no son null en el DTO
            if (recetaUpdateDto.getDescripcion() != null && 
                !recetaUpdateDto.getDescripcion().trim().isEmpty()) {
                
                // Validar la nueva descripción
                if (!validarDescripcionReceta(recetaUpdateDto.getDescripcion())) {
                    throw new RuntimeException("La nueva descripción de la receta contiene información no válida");
                }
                
                if (recetaUpdateDto.getDescripcion().trim().length() < 10) {
                    throw new RuntimeException("La descripción de la receta debe tener al menos 10 caracteres");
                }
                
                // Mantener un historial de cambios (opcional)
                String descripcionAnterior = receta.getDescripcion();
                receta.setDescripcion(normalizarDescripcion(recetaUpdateDto.getDescripcion()));
                
                // Log del cambio para auditoría
                System.out.println("AUDITORÍA: Receta ID " + id + " modificada. Descripción anterior: " + 
                        descripcionAnterior.substring(0, Math.min(50, descripcionAnterior.length())) + "...");
            }
            
            // Note: La fecha de la receta y la cita asociada no se pueden cambiar una vez creada
            
            Receta updatedReceta = recetaRepository.save(receta);
            return mapToResponseDto(updatedReceta);
        }
        
        throw new RuntimeException("Receta no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Receta> receta = recetaRepository.findById(id);
        
        if (receta.isPresent()) {
            Receta recetaEntity = receta.get();
            
            // Verificar que la receta no sea muy antigua (regla de negocio)
            if (recetaEntity.getFechaReceta() != null && 
                recetaEntity.getFechaReceta().isBefore(LocalDate.now().minusDays(3))) {
                throw new RuntimeException("No se puede eliminar una receta con más de 3 días de antigüedad");
            }
            
            // Verificar el estado de la cita asociada
            if (recetaEntity.getCita() != null) {
                Cita cita = recetaEntity.getCita();
                
                if (cita.getEstado() == EstadoCitaEnum.COMPLETADA && 
                    cita.getFechaHoraFin() != null &&
                    cita.getFechaHoraFin().toLocalDate().isBefore(LocalDate.now().minusDays(7))) {
                    throw new RuntimeException("No se puede eliminar una receta de una cita completada hace más de 7 días");
                }
            }
            
            // Log de eliminación para auditoría
            System.out.println("AUDITORÍA: Eliminando receta ID " + id + " - Fecha: " + 
                    recetaEntity.getFechaReceta() + " - Descripción: " + 
                    recetaEntity.getDescripcion().substring(0, Math.min(50, recetaEntity.getDescripcion().length())) + "...");
            
            recetaRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de recetas
    private boolean validarDescripcionReceta(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return false;
        }
        
        String desc = descripcion.toLowerCase().trim();
        
        // Verificar que contenga información médica básica
        String[] palabrasRequeridas = {"mg", "ml", "tableta", "cápsula", "jarabe", "cada", "horas", "días", "veces"};
        boolean contieneTerminoMedico = Arrays.stream(palabrasRequeridas)
                .anyMatch(desc::contains);
        
        // Verificar que no contenga términos inapropiados
        String[] terminosProhibidos = {"xxx", "drogas", "ilegal"};
        boolean contieneTerminoProhibido = Arrays.stream(terminosProhibidos)
                .anyMatch(desc::contains);
        
        return contieneTerminoMedico && !contieneTerminoProhibido;
    }
    
    private String normalizarDescripcion(String descripcion) {
        if (descripcion == null) return null;
        
        // Limpiar espacios múltiples y normalizar formato
        String normalizada = descripcion.trim().replaceAll("\\s+", " ");
        
        // Capitalizar primera letra
        if (!normalizada.isEmpty()) {
            normalizada = normalizada.substring(0, 1).toUpperCase() + 
                         normalizada.substring(1).toLowerCase();
        }
        
        return normalizada;
    }
    
    public List<Receta> getRecetasPorDoctor(Integer idDoctor) {
        return recetaRepository.findAll().stream()
                .filter(r -> r.getCita() != null && 
                        r.getCita().getDoctor() != null &&
                        r.getCita().getDoctor().getIdUsuario().equals(idDoctor))
                .collect(Collectors.toList());
    }
    
    public List<Receta> getRecetasPorPaciente(Integer idPaciente) {
        return recetaRepository.findAll().stream()
                .filter(r -> r.getCita() != null && 
                        r.getCita().getPaciente() != null &&
                        r.getCita().getPaciente().getIdUsuario().equals(idPaciente))
                .collect(Collectors.toList());
    }
    
    public List<Receta> getRecetasRecientes(int dias) {
        LocalDate fechaLimite = LocalDate.now().minusDays(dias);
        return recetaRepository.findAll().stream()
                .filter(r -> r.getFechaReceta() != null && 
                        !r.getFechaReceta().isBefore(fechaLimite))
                .collect(Collectors.toList());
    }
    
    public int contarRecetasPorCita(Integer idCita) {
        return (int) recetaRepository.findAll().stream()
                .filter(r -> r.getCita() != null && 
                        r.getCita().getIdCita().equals(idCita))
                .count();
    }

    // Método helper para mapear a ResponseDto con información adicional
    private RecetaResponseDto mapToResponseDto(Receta receta) {
        RecetaResponseDto responseDto = modelMapper.map(receta, RecetaResponseDto.class);
        
        // Agregar información adicional de la cita si está disponible
        if (receta.getCita() != null) {
            responseDto.setIdCita(receta.getCita().getIdCita());
            responseDto.setMotivoCita(receta.getCita().getMotivo());
            
            // Agregar información del doctor
            if (receta.getCita().getDoctor() != null && 
                receta.getCita().getDoctor().getPersona() != null) {
                String nombreDoctor = receta.getCita().getDoctor().getPersona().getNombres() + 
                        " " + receta.getCita().getDoctor().getPersona().getApellidos();
                responseDto.setNombreDoctor(nombreDoctor);
            }
            
            // Agregar información del paciente
            if (receta.getCita().getPaciente() != null && 
                receta.getCita().getPaciente().getPersona() != null) {
                String nombrePaciente = receta.getCita().getPaciente().getPersona().getNombres() + 
                        " " + receta.getCita().getPaciente().getPersona().getApellidos();
                responseDto.setNombrePaciente(nombrePaciente);
            }
        }
        
        return responseDto;
    }
}