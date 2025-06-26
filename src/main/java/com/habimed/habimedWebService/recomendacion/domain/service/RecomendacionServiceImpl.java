package com.habimed.habimedWebService.recomendacion.domain.service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import com.habimed.habimedWebService.recomendacion.dto.RecomendacionResponseDto;
import com.habimed.habimedWebService.recomendacion.dto.RecomendacionUpdateDto;
import com.habimed.habimedWebService.recomendacion.repository.RecomendacionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecomendacionServiceImpl implements RecomendacionService {
    
    private final RecomendacionRepository recomendacionRepository;
    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Recomendacion> findAll() {
        return recomendacionRepository.findAll();
    }

    @Override
    public List<Recomendacion> findAllWithConditions(com.habimed.habimedWebService.recomendacion.dto.RecomendacionFilterDto recomendacionFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Recomendacion> recomendaciones = recomendacionRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (recomendacionFilterDto.getIdCita() != null) {
            recomendaciones = recomendaciones.stream()
                    .filter(r -> r.getCita() != null && 
                            r.getCita().getIdCita().equals(recomendacionFilterDto.getIdCita()))
                    .collect(Collectors.toList());
        }
        
        if (recomendacionFilterDto.getDescripcionContiene() != null && 
            !recomendacionFilterDto.getDescripcionContiene().trim().isEmpty()) {
            String descripcionBuscada = recomendacionFilterDto.getDescripcionContiene().toLowerCase().trim();
            recomendaciones = recomendaciones.stream()
                    .filter(r -> r.getDescripcion() != null && 
                            r.getDescripcion().toLowerCase().contains(descripcionBuscada))
                    .collect(Collectors.toList());
        }
        
        if (recomendacionFilterDto.getFechaRecomendacionInicio() != null) {
            recomendaciones = recomendaciones.stream()
                    .filter(r -> r.getFechaRecomendacion() != null && 
                            !r.getFechaRecomendacion().isBefore(recomendacionFilterDto.getFechaRecomendacionInicio()))
                    .collect(Collectors.toList());
        }
        
        if (recomendacionFilterDto.getFechaRecomendacionFin() != null) {
            recomendaciones = recomendaciones.stream()
                    .filter(r -> r.getFechaRecomendacion() != null && 
                            !r.getFechaRecomendacion().isAfter(recomendacionFilterDto.getFechaRecomendacionFin()))
                    .collect(Collectors.toList());
        }
        
        return recomendaciones;
    }

    @Override
    public com.habimed.habimedWebService.recomendacion.dto.RecomendacionResponseDto getById(Integer id) {
        Optional<Recomendacion> recomendacion = recomendacionRepository.findById(id);
        if (recomendacion.isPresent()) {
            return mapToResponseDto(recomendacion.get());
        }
        throw new RuntimeException("Recomendación no encontrada con ID: " + id);
    }

    @Override
    public com.habimed.habimedWebService.recomendacion.dto.RecomendacionResponseDto save(com.habimed.habimedWebService.recomendacion.dto.RecomendacionInsertDto recomendacionInsertDto) {
        // Validaciones específicas del contexto de Recomendación
        if (recomendacionInsertDto.getIdCita() == null) {
            throw new RuntimeException("El ID de la cita es obligatorio para crear una recomendación");
        }
        
        // Verificar que la cita existe
        Optional<Cita> cita = citaRepository.findById(recomendacionInsertDto.getIdCita());
        if (!cita.isPresent()) {
            throw new RuntimeException("No existe una cita con ID: " + recomendacionInsertDto.getIdCita());
        }
        
        Cita citaEntity = cita.get();
        
        // Verificar que la cita esté completada o en proceso (no se pueden crear recomendaciones para citas futuras o canceladas)
        if (citaEntity.getEstado() == EstadoCitaEnum.CANCELADA) {
            throw new RuntimeException("No se puede crear una recomendación para una cita cancelada");
        }
        
        if (citaEntity.getEstado() == EstadoCitaEnum.PROGRAMADA && 
            citaEntity.getFechaHoraInicio() != null && 
            citaEntity.getFechaHoraInicio().toLocalDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("No se puede crear una recomendación para una cita que aún no ha ocurrido");
        }
        
        // Verificar que el doctor esté asignado a la cita
        if (citaEntity.getDoctor() == null) {
            throw new RuntimeException("No se puede crear una recomendación para una cita sin doctor asignado");
        }
        
        // Validar que el doctor sea realmente un doctor
        if (citaEntity.getDoctor().getTipoUsuario() != com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum.DOCTOR) {
            throw new RuntimeException("Solo un doctor puede crear recomendaciones médicas");
        }
        
        // Validar contenido de la descripción
        if (!validarDescripcionRecomendacion(recomendacionInsertDto.getDescripcion())) {
            throw new RuntimeException("La descripción de la recomendación contiene información no válida o está vacía");
        }
        
        // Verificar longitud mínima de descripción
        if (recomendacionInsertDto.getDescripcion().trim().length() < 10) {
            throw new RuntimeException("La descripción de la recomendación debe tener al menos 10 caracteres");
        }
        
        // Verificar que no se dupliquen recomendaciones idénticas para la misma cita
        List<Recomendacion> recomendacionesExistentes = recomendacionRepository.findAll();
        boolean recomendacionDuplicada = recomendacionesExistentes.stream()
                .anyMatch(r -> r.getCita() != null && 
                        r.getCita().getIdCita().equals(recomendacionInsertDto.getIdCita()) &&
                        r.getDescripcion().equalsIgnoreCase(recomendacionInsertDto.getDescripcion().trim()));
        
        if (recomendacionDuplicada) {
            throw new RuntimeException("Ya existe una recomendación idéntica para esta cita");
        }
        
        Recomendacion recomendacion = modelMapper.map(recomendacionInsertDto, Recomendacion.class);
        recomendacion.setCita(citaEntity);
        
        // Establecer fecha actual si no se proporciona
        if (recomendacion.getFechaRecomendacion() == null) {
            recomendacion.setFechaRecomendacion(LocalDate.now());
        }
        
        // Validar que la fecha de la recomendación no sea anterior a la fecha de la cita
        if (citaEntity.getFechaHoraInicio() != null && 
            recomendacion.getFechaRecomendacion().isBefore(citaEntity.getFechaHoraInicio().toLocalDate())) {
            throw new RuntimeException("La fecha de la recomendación no puede ser anterior a la fecha de la cita");
        }
        
        // Validar que la fecha de la recomendación no sea futura (más de 1 día)
        if (recomendacion.getFechaRecomendacion().isAfter(LocalDate.now().plusDays(1))) {
            throw new RuntimeException("La fecha de la recomendación no puede ser más de 1 día en el futuro");
        }
        
        // Normalizar y limpiar la descripción
        recomendacion.setDescripcion(normalizarDescripcion(recomendacion.getDescripcion()));
        
        Recomendacion savedRecomendacion = recomendacionRepository.save(recomendacion);
        return mapToResponseDto(savedRecomendacion);
    }

    @Override
    public RecomendacionResponseDto update(Integer id, RecomendacionUpdateDto recomendacionUpdateDto) {
        Optional<Recomendacion> existingRecomendacion = recomendacionRepository.findById(id);
        
        if (existingRecomendacion.isPresent()) {
            Recomendacion recomendacion = existingRecomendacion.get();
            
            // Verificar que la recomendación no sea muy antigua (regla de negocio para modificaciones)
            if (recomendacion.getFechaRecomendacion() != null && 
                recomendacion.getFechaRecomendacion().isBefore(LocalDate.now().minusDays(7))) {
                throw new RuntimeException("No se puede modificar una recomendación con más de 7 días de antigüedad");
            }
            
            // Verificar que la cita asociada no esté completada hace mucho tiempo
            if (recomendacion.getCita() != null && recomendacion.getCita().getEstado() == EstadoCitaEnum.COMPLETADA &&
                recomendacion.getCita().getFechaHoraFin() != null &&
                recomendacion.getCita().getFechaHoraFin().toLocalDate().isBefore(LocalDate.now().minusDays(30))) {
                throw new RuntimeException("No se puede modificar una recomendación de una cita completada hace más de 30 días");
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (recomendacionUpdateDto.getDescripcion() != null && 
                !recomendacionUpdateDto.getDescripcion().trim().isEmpty()) {
                
                // Validar la nueva descripción
                if (!validarDescripcionRecomendacion(recomendacionUpdateDto.getDescripcion())) {
                    throw new RuntimeException("La nueva descripción de la recomendación contiene información no válida");
                }
                
                if (recomendacionUpdateDto.getDescripcion().trim().length() < 10) {
                    throw new RuntimeException("La descripción de la recomendación debe tener al menos 10 caracteres");
                }
                
                // Verificar que no se duplique con otras recomendaciones de la misma cita
                if (recomendacion.getCita() != null) {
                    List<Recomendacion> recomendacionesCita = recomendacionRepository.findAll().stream()
                            .filter(r -> r.getCita() != null && 
                                    r.getCita().getIdCita().equals(recomendacion.getCita().getIdCita()) &&
                                    !r.getIdRecomendacion().equals(id))
                            .collect(Collectors.toList());
                    
                    boolean descripcionDuplicada = recomendacionesCita.stream()
                            .anyMatch(r -> r.getDescripcion().equalsIgnoreCase(recomendacionUpdateDto.getDescripcion().trim()));
                    
                    if (descripcionDuplicada) {
                        throw new RuntimeException("Ya existe otra recomendación con la misma descripción para esta cita");
                    }
                }
                
                // Mantener un historial de cambios (opcional)
                String descripcionAnterior = recomendacion.getDescripcion();
                recomendacion.setDescripcion(normalizarDescripcion(recomendacionUpdateDto.getDescripcion()));
                
                // Log del cambio para auditoría
                System.out.println("AUDITORÍA: Recomendación ID " + id + " modificada. Descripción anterior: " + 
                        descripcionAnterior.substring(0, Math.min(50, descripcionAnterior.length())) + "...");
            }
            
            // Note: La fecha de la recomendación y la cita asociada no se pueden cambiar una vez creada
            
            Recomendacion updatedRecomendacion = recomendacionRepository.save(recomendacion);
            return mapToResponseDto(updatedRecomendacion);
        }
        
        throw new RuntimeException("Recomendación no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Recomendacion> recomendacion = recomendacionRepository.findById(id);
        
        if (recomendacion.isPresent()) {
            Recomendacion recomendacionEntity = recomendacion.get();
            
            // Verificar que la recomendación no sea muy antigua (regla de negocio)
            if (recomendacionEntity.getFechaRecomendacion() != null && 
                recomendacionEntity.getFechaRecomendacion().isBefore(LocalDate.now().minusDays(3))) {
                throw new RuntimeException("No se puede eliminar una recomendación con más de 3 días de antigüedad");
            }
            
            // Verificar el estado de la cita asociada
            if (recomendacionEntity.getCita() != null) {
                Cita cita = recomendacionEntity.getCita();
                
                if (cita.getEstado() == EstadoCitaEnum.COMPLETADA && 
                    cita.getFechaHoraFin() != null &&
                    cita.getFechaHoraFin().toLocalDate().isBefore(LocalDate.now().minusDays(7))) {
                    throw new RuntimeException("No se puede eliminar una recomendación de una cita completada hace más de 7 días");
                }
            }
            
            // Log de eliminación para auditoría
            System.out.println("AUDITORÍA: Eliminando recomendación ID " + id + " - Fecha: " + 
                    recomendacionEntity.getFechaRecomendacion() + " - Descripción: " + 
                    recomendacionEntity.getDescripcion().substring(0, Math.min(50, recomendacionEntity.getDescripcion().length())) + "...");
            
            recomendacionRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de recomendaciones
    private boolean validarDescripcionRecomendacion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return false;
        }
        
        String desc = descripcion.toLowerCase().trim();
        
        // Verificar que contenga términos de recomendaciones médicas
        String[] palabrasRecomendacion = {
            "ejercicio", "dieta", "reposo", "consultar", "seguimiento", "control", 
            "evitar", "consumir", "aplicar", "mantener", "realizar", "descanso",
            "hidratación", "alimentación", "actividad", "fisioterapia", "cuidado"
        };
        
        boolean contieneTerminoMedico = Arrays.stream(palabrasRecomendacion)
                .anyMatch(desc::contains);
        
        // Verificar que no contenga términos inapropiados
        String[] terminosProhibidos = {"xxx", "drogas ilegales", "automedicarse"};
        boolean contieneTerminoProhibido = Arrays.stream(terminosProhibidos)
                .anyMatch(desc::contains);
        
        return contieneTerminoMedico && !contieneTerminoProhibido;
    }
    
    private String normalizarDescripcion(String descripcion) {
        if (descripcion == null) return null;
        
        // Limpiar espacios múltiples y normalizar formato
        String normalizada = descripcion.trim().replaceAll("\\s+", " ");
        
        // Capitalizar primera letra de cada oración
        String[] oraciones = normalizada.split("\\. ");
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < oraciones.length; i++) {
            String oracion = oraciones[i].trim();
            if (!oracion.isEmpty()) {
                oracion = oracion.substring(0, 1).toUpperCase() + oracion.substring(1).toLowerCase();
                resultado.append(oracion);
                if (i < oraciones.length - 1) {
                    resultado.append(". ");
                }
            }
        }
        
        // Asegurar que termine con punto si no lo tiene
        String final_result = resultado.toString();
        if (!final_result.endsWith(".") && !final_result.endsWith("!") && !final_result.endsWith("?")) {
            final_result += ".";
        }
        
        return final_result;
    }
    
    public List<Recomendacion> getRecomendacionesPorDoctor(Integer idDoctor) {
        return recomendacionRepository.findAll().stream()
                .filter(r -> r.getCita() != null && 
                        r.getCita().getDoctor() != null &&
                        r.getCita().getDoctor().getIdUsuario().equals(idDoctor))
                .collect(Collectors.toList());
    }
    
    public List<Recomendacion> getRecomendacionesPorPaciente(Integer idPaciente) {
        return recomendacionRepository.findAll().stream()
                .filter(r -> r.getCita() != null && 
                        r.getCita().getPaciente() != null &&
                        r.getCita().getPaciente().getIdUsuario().equals(idPaciente))
                .collect(Collectors.toList());
    }
    
    public List<Recomendacion> getRecomendacionesRecientes(int dias) {
        LocalDate fechaLimite = LocalDate.now().minusDays(dias);
        return recomendacionRepository.findAll().stream()
                .filter(r -> r.getFechaRecomendacion() != null && 
                        !r.getFechaRecomendacion().isBefore(fechaLimite))
                .collect(Collectors.toList());
    }
    
    public int contarRecomendacionesPorCita(Integer idCita) {
        return (int) recomendacionRepository.findAll().stream()
                .filter(r -> r.getCita() != null && 
                        r.getCita().getIdCita().equals(idCita))
                .count();
    }
    
    public List<Recomendacion> getRecomendacionesPorTipo(String tipoRecomendacion) {
        return recomendacionRepository.findAll().stream()
                .filter(r -> r.getDescripcion() != null && 
                        r.getDescripcion().toLowerCase().contains(tipoRecomendacion.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Método helper para mapear a ResponseDto con información adicional
    private RecomendacionResponseDto mapToResponseDto(Recomendacion recomendacion) {
        RecomendacionResponseDto responseDto = modelMapper.map(recomendacion, RecomendacionResponseDto.class);
        
        // Agregar información adicional de la cita si está disponible
        if (recomendacion.getCita() != null) {
            responseDto.setIdCita(recomendacion.getCita().getIdCita());
            responseDto.setMotivoCita(recomendacion.getCita().getMotivo());
            
            // Agregar información del doctor
            if (recomendacion.getCita().getDoctor() != null && 
                recomendacion.getCita().getDoctor().getPersona() != null) {
                String nombreDoctor = recomendacion.getCita().getDoctor().getPersona().getNombres() + 
                        " " + recomendacion.getCita().getDoctor().getPersona().getApellidos();
                responseDto.setNombreDoctor(nombreDoctor);
            }
            
            // Agregar información del paciente
            if (recomendacion.getCita().getPaciente() != null && 
                recomendacion.getCita().getPaciente().getPersona() != null) {
                String nombrePaciente = recomendacion.getCita().getPaciente().getPersona().getNombres() + 
                        " " + recomendacion.getCita().getPaciente().getPersona().getApellidos();
                responseDto.setNombrePaciente(nombrePaciente);
            }
        }
        
        // Categorizar el tipo de recomendación basado en el contenido
        // responseDto.setTipoRecomendacion(categorizarRecomendacion(recomendacion.getDescripcion()));
        
        return responseDto;
    }
    
    private String categorizarRecomendacion(String descripcion) {
        if (descripcion == null) return "GENERAL";
        
        String desc = descripcion.toLowerCase();
        
        if (desc.contains("ejercicio") || desc.contains("actividad física") || desc.contains("caminar")) {
            return "EJERCICIO";
        } else if (desc.contains("dieta") || desc.contains("alimentación") || desc.contains("comida")) {
            return "ALIMENTACIÓN";
        } else if (desc.contains("reposo") || desc.contains("descanso") || desc.contains("dormir")) {
            return "REPOSO";
        } else if (desc.contains("medicamento") || desc.contains("medicina") || desc.contains("tomar")) {
            return "MEDICACIÓN";
        } else if (desc.contains("seguimiento") || desc.contains("control") || desc.contains("consultar")) {
            return "SEGUIMIENTO";
        } else if (desc.contains("higiene") || desc.contains("limpieza") || desc.contains("cuidado")) {
            return "HIGIENE";
        } else {
            return "GENERAL";
        }
    }
}