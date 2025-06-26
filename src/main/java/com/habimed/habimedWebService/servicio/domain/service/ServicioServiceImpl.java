package com.habimed.habimedWebService.servicio.domain.service;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.repository.EspecialidadRepository;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.dto.ServicioFilterDto;
import com.habimed.habimedWebService.servicio.dto.ServicioInsertDto;
import com.habimed.habimedWebService.servicio.dto.ServicioResponseDto;
import com.habimed.habimedWebService.servicio.dto.ServicioUpdateDto;
import com.habimed.habimedWebService.servicio.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {
    
    private final ServicioRepository servicioRepository;
    private final EspecialidadRepository especialidadRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    public List<Servicio> findAllWithConditions(ServicioFilterDto servicioFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Servicio> servicios = servicioRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (servicioFilterDto.getIdEspecialidad() != null) {
            servicios = servicios.stream()
                    .filter(s -> s.getEspecialidad() != null && 
                            s.getEspecialidad().getIdEspecialidad().equals(servicioFilterDto.getIdEspecialidad()))
                    .collect(Collectors.toList());
        }
        
        if (servicioFilterDto.getNombre() != null && !servicioFilterDto.getNombre().trim().isEmpty()) {
            String nombreBuscado = servicioFilterDto.getNombre().toLowerCase().trim();
            servicios = servicios.stream()
                    .filter(s -> s.getNombre() != null && 
                            s.getNombre().toLowerCase().contains(nombreBuscado))
                    .collect(Collectors.toList());
        }
        
        return servicios;
    }

    @Override
    public ServicioResponseDto getById(Integer id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        if (servicio.isPresent()) {
            return mapToResponseDto(servicio.get());
        }
        throw new RuntimeException("Servicio no encontrado con ID: " + id);
    }

    @Override
    public ServicioResponseDto save(ServicioInsertDto servicioInsertDto) {
        // Validaciones específicas del contexto de Servicio
        if (servicioInsertDto.getIdEspecialidad() == null) {
            throw new RuntimeException("El ID de la especialidad es obligatorio para crear un servicio");
        }
        
        // Verificar que la especialidad existe
        Optional<Especialidad> especialidad = especialidadRepository.findById(servicioInsertDto.getIdEspecialidad());
        if (!especialidad.isPresent()) {
            throw new RuntimeException("No existe una especialidad con ID: " + servicioInsertDto.getIdEspecialidad());
        }
        
        Especialidad especialidadEntity = especialidad.get();
        
        // Validar nombre del servicio
        if (servicioInsertDto.getNombre() == null || servicioInsertDto.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del servicio es obligatorio");
        }
        
        if (!validarNombreServicio(servicioInsertDto.getNombre())) {
            throw new RuntimeException("El nombre del servicio contiene caracteres no válidos");
        }
        
        if (servicioInsertDto.getNombre().trim().length() < 3) {
            throw new RuntimeException("El nombre del servicio debe tener al menos 3 caracteres");
        }
        
        if (servicioInsertDto.getNombre().trim().length() > 100) {
            throw new RuntimeException("El nombre del servicio no puede exceder 100 caracteres");
        }
        
        // Verificar que no exista un servicio con el mismo nombre en la misma especialidad
        List<Servicio> serviciosExistentes = servicioRepository.findAll();
        boolean nombreDuplicado = serviciosExistentes.stream()
                .anyMatch(s -> s.getEspecialidad() != null && 
                        s.getEspecialidad().getIdEspecialidad().equals(servicioInsertDto.getIdEspecialidad()) &&
                        s.getNombre() != null &&
                        s.getNombre().trim().equalsIgnoreCase(servicioInsertDto.getNombre().trim()));
        
        if (nombreDuplicado) {
            throw new RuntimeException("Ya existe un servicio con el nombre '" + 
                    servicioInsertDto.getNombre().trim() + "' en esta especialidad");
        }
        
        // Validar descripción si se proporciona
        if (servicioInsertDto.getDescripcion() != null && !servicioInsertDto.getDescripcion().trim().isEmpty()) {
            if (servicioInsertDto.getDescripcion().trim().length() < 10) {
                throw new RuntimeException("La descripción debe tener al menos 10 caracteres");
            }
            
            if (servicioInsertDto.getDescripcion().trim().length() > 500) {
                throw new RuntimeException("La descripción no puede exceder 500 caracteres");
            }
            
            if (!validarTextoDescriptivo(servicioInsertDto.getDescripcion())) {
                throw new RuntimeException("La descripción contiene contenido no válido");
            }
        }
        
        // Validar riesgos si se proporciona
        if (servicioInsertDto.getRiesgos() != null && !servicioInsertDto.getRiesgos().trim().isEmpty()) {
            if (servicioInsertDto.getRiesgos().trim().length() < 5) {
                throw new RuntimeException("La descripción de riesgos debe tener al menos 5 caracteres");
            }
            
            if (servicioInsertDto.getRiesgos().trim().length() > 500) {
                throw new RuntimeException("La descripción de riesgos no puede exceder 500 caracteres");
            }
            
            if (!validarTextoDescriptivo(servicioInsertDto.getRiesgos())) {
                throw new RuntimeException("La descripción de riesgos contiene contenido no válido");
            }
        }
        
        Servicio servicio = modelMapper.map(servicioInsertDto, Servicio.class);
        servicio.setEspecialidad(especialidadEntity);
        
        // Normalizar textos
        servicio.setNombre(normalizarTexto(servicio.getNombre()));
        
        if (servicio.getDescripcion() != null && !servicio.getDescripcion().trim().isEmpty()) {
            servicio.setDescripcion(normalizarTexto(servicio.getDescripcion()));
        }
        
        if (servicio.getRiesgos() != null && !servicio.getRiesgos().trim().isEmpty()) {
            servicio.setRiesgos(normalizarTexto(servicio.getRiesgos()));
        }
        
        Servicio savedServicio = servicioRepository.save(servicio);
        return mapToResponseDto(savedServicio);
    }

    @Override
    public ServicioResponseDto update(Integer id, ServicioUpdateDto servicioUpdateDto) {
        Optional<Servicio> existingServicio = servicioRepository.findById(id);
        
        if (existingServicio.isPresent()) {
            Servicio servicio = existingServicio.get();
            
            // Verificar si el servicio tiene citas asociadas antes de permitir ciertos cambios
            // (Esta validación dependería de tener acceso al CitaRepository)
            
            // Actualizar solo los campos que no son null en el DTO
            if (servicioUpdateDto.getNombre() != null && !servicioUpdateDto.getNombre().trim().isEmpty()) {
                if (!validarNombreServicio(servicioUpdateDto.getNombre())) {
                    throw new RuntimeException("El nombre del servicio contiene caracteres no válidos");
                }
                
                if (servicioUpdateDto.getNombre().trim().length() < 3) {
                    throw new RuntimeException("El nombre del servicio debe tener al menos 3 caracteres");
                }
                
                if (servicioUpdateDto.getNombre().trim().length() > 100) {
                    throw new RuntimeException("El nombre del servicio no puede exceder 100 caracteres");
                }
                
                // Verificar que no exista otro servicio con el mismo nombre en la misma especialidad
                List<Servicio> serviciosExistentes = servicioRepository.findAll();
                boolean nombreDuplicado = serviciosExistentes.stream()
                        .anyMatch(s -> !s.getIdServicio().equals(id) && // Excluir el servicio actual
                                s.getEspecialidad() != null && 
                                s.getEspecialidad().getIdEspecialidad().equals(servicio.getEspecialidad().getIdEspecialidad()) &&
                                s.getNombre() != null &&
                                s.getNombre().trim().equalsIgnoreCase(servicioUpdateDto.getNombre().trim()));
                
                if (nombreDuplicado) {
                    throw new RuntimeException("Ya existe otro servicio con el nombre '" + 
                            servicioUpdateDto.getNombre().trim() + "' en esta especialidad");
                }
                
                servicio.setNombre(normalizarTexto(servicioUpdateDto.getNombre()));
            }
            
            if (servicioUpdateDto.getDescripcion() != null) {
                if (!servicioUpdateDto.getDescripcion().trim().isEmpty()) {
                    if (servicioUpdateDto.getDescripcion().trim().length() < 10) {
                        throw new RuntimeException("La descripción debe tener al menos 10 caracteres");
                    }
                    
                    if (servicioUpdateDto.getDescripcion().trim().length() > 500) {
                        throw new RuntimeException("La descripción no puede exceder 500 caracteres");
                    }
                    
                    if (!validarTextoDescriptivo(servicioUpdateDto.getDescripcion())) {
                        throw new RuntimeException("La descripción contiene contenido no válido");
                    }
                    
                    servicio.setDescripcion(normalizarTexto(servicioUpdateDto.getDescripcion()));
                } else {
                    // Vaciar descripción si se envía cadena vacía
                    servicio.setDescripcion(null);
                }
            }
            
            if (servicioUpdateDto.getRiesgos() != null) {
                if (!servicioUpdateDto.getRiesgos().trim().isEmpty()) {
                    if (servicioUpdateDto.getRiesgos().trim().length() < 5) {
                        throw new RuntimeException("La descripción de riesgos debe tener al menos 5 caracteres");
                    }
                    
                    if (servicioUpdateDto.getRiesgos().trim().length() > 500) {
                        throw new RuntimeException("La descripción de riesgos no puede exceder 500 caracteres");
                    }
                    
                    if (!validarTextoDescriptivo(servicioUpdateDto.getRiesgos())) {
                        throw new RuntimeException("La descripción de riesgos contiene contenido no válido");
                    }
                    
                    servicio.setRiesgos(normalizarTexto(servicioUpdateDto.getRiesgos()));
                } else {
                    // Vaciar riesgos si se envía cadena vacía
                    servicio.setRiesgos(null);
                }
            }
            
            Servicio updatedServicio = servicioRepository.save(servicio);
            return mapToResponseDto(updatedServicio);
        }
        
        throw new RuntimeException("Servicio no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        
        if (servicio.isPresent()) {
            Servicio servicioEntity = servicio.get();
            
            // Verificar que el servicio no tenga citas asociadas (regla de negocio)
            // Esta validación requeriría acceso al CitaRepository para verificar relaciones
            
            // Verificar que el servicio no esté asociado a consultorios activos
            if (servicioEntity.getConsultorios() != null && !servicioEntity.getConsultorios().isEmpty()) {
                throw new RuntimeException("No se puede eliminar un servicio que está asociado a consultorios. " +
                        "Primero debe desasociar el servicio de todos los consultorios.");
            }
            
            // Log de eliminación para auditoría
            System.out.println("AUDITORÍA: Eliminando servicio ID " + id + " - Nombre: " + 
                    servicioEntity.getNombre() + " - Especialidad ID: " + 
                    (servicioEntity.getEspecialidad() != null ? 
                     servicioEntity.getEspecialidad().getIdEspecialidad() : "null"));
            
            servicioRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de servicios
    private boolean validarNombreServicio(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        // Permitir letras, números, espacios, guiones y algunos caracteres especiales médicos
        return nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s\\-\\.\\(\\)]+$");
    }
    
    private boolean validarTextoDescriptivo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return true; // Texto vacío es válido para campos opcionales
        }
        
        String textoLimpio = texto.toLowerCase().trim();
        
        // Verificar términos inapropiados (lista básica)
        String[] terminosProhibidos = {
            "maldito", "estúpido", "idiota", "basura", "mierda"
        };
        
        boolean contieneTerminoProhibido = Arrays.stream(terminosProhibidos)
                .anyMatch(textoLimpio::contains);
        
        // Verificar que no sea solo caracteres especiales
        boolean soloCaracteresEspeciales = !textoLimpio.matches(".*[a-záéíóúñ0-9].*");
        
        return !contieneTerminoProhibido && !soloCaracteresEspeciales;
    }
    
    private String normalizarTexto(String texto) {
        if (texto == null) return null;
        
        // Limpiar espacios múltiples y normalizar formato
        String normalizado = texto.trim().replaceAll("\\s+", " ");
        
        // Capitalizar primera letra de cada palabra para nombres
        if (!normalizado.isEmpty()) {
            String[] palabras = normalizado.split(" ");
            StringBuilder resultado = new StringBuilder();
            
            for (int i = 0; i < palabras.length; i++) {
                if (i > 0) resultado.append(" ");
                
                String palabra = palabras[i].toLowerCase();
                if (!palabra.isEmpty()) {
                    // Capitalizar primera letra
                    resultado.append(palabra.substring(0, 1).toUpperCase())
                            .append(palabra.substring(1));
                }
            }
            
            normalizado = resultado.toString();
        }
        
        return normalizado;
    }

    // Método helper para mapear a ResponseDto
    private ServicioResponseDto mapToResponseDto(Servicio servicio) {
        ServicioResponseDto responseDto = modelMapper.map(servicio, ServicioResponseDto.class);
        
        // Agregar información adicional de la especialidad
        /*if (servicio.getEspecialidad() != null) {
            responseDto.setIdEspecialidad(servicio.getEspecialidad().getIdEspecialidad());
            responseDto.setNombreEspecialidad(servicio.getEspecialidad().getNombre());
        }
        
        // Agregar información de consultorios asociados
        if (servicio.getConsultorios() != null) {
            responseDto.setNumeroConsultoriosAsociados(servicio.getConsultorios().size());
        }*/
        
        return responseDto;
    }
}