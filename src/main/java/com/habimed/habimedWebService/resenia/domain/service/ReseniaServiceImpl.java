package com.habimed.habimedWebService.resenia.domain.service;

import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.resenia.domain.model.Resenia;
import com.habimed.habimedWebService.resenia.dto.ReseniaFilterDto;
import com.habimed.habimedWebService.resenia.dto.ReseniaInsertDto;
import com.habimed.habimedWebService.resenia.dto.ReseniaResponseDto;
import com.habimed.habimedWebService.resenia.dto.ReseniaUpdateDto;
import com.habimed.habimedWebService.resenia.repository.ReseniaRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReseniaServiceImpl implements ReseniaService {
    
    private final ReseniaRepository reseniaRepository
            ;
    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Resenia> findAll() {
        return reseniaRepository.findAll();
    }

    @Override
    public List<Resenia> findAllWithConditions(ReseniaFilterDto reseniaFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Resenia> resenias = reseniaRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (reseniaFilterDto.getIdDoctor() != null) {
            resenias = resenias.stream()
                    .filter(r -> r.getDoctor() != null && 
                            r.getDoctor().getIdUsuario().equals(reseniaFilterDto.getIdDoctor()))
                    .collect(Collectors.toList());
        }
        
        if (reseniaFilterDto.getCalificacionMinima() != null) {
            resenias = resenias.stream()
                    .filter(r -> r.getCalificacion() != null && 
                            r.getCalificacion().compareTo(reseniaFilterDto.getCalificacionMinima()) >= 0)
                    .collect(Collectors.toList());
        }
        
        if (reseniaFilterDto.getCalificacionMaxima() != null) {
            resenias = resenias.stream()
                    .filter(r -> r.getCalificacion() != null && 
                            r.getCalificacion().compareTo(reseniaFilterDto.getCalificacionMaxima()) <= 0)
                    .collect(Collectors.toList());
        }
        
        if (reseniaFilterDto.getFechaInicio() != null) {
            resenias = resenias.stream()
                    .filter(r -> r.getFechaResenia() != null && 
                            !r.getFechaResenia().isBefore(reseniaFilterDto.getFechaInicio()))
                    .collect(Collectors.toList());
        }
        
        if (reseniaFilterDto.getFechaFin() != null) {
            resenias = resenias.stream()
                    .filter(r -> r.getFechaResenia() != null && 
                            !r.getFechaResenia().isAfter(reseniaFilterDto.getFechaFin()))
                    .collect(Collectors.toList());
        }
        
        return resenias;
    }

    @Override
    public ReseniaResponseDto getById(Integer id) {
        Optional<Resenia> resenia = reseniaRepository.findById(id);
        if (((Optional<?>) resenia).isPresent()) {
            return mapToResponseDto(resenia.get());
        }
        throw new RuntimeException("Reseña no encontrada con ID: " + id);
    }

    @Override
    public ReseniaResponseDto save(ReseniaInsertDto reseniaInsertDto) {
        // Validaciones específicas del contexto de Reseña
        if (reseniaInsertDto.getIdDoctor() == null) {
            throw new RuntimeException("El ID del doctor es obligatorio para crear una reseña");
        }
        
        // Verificar que el doctor existe
        Optional<Usuario> doctor = usuarioRepository.findById(reseniaInsertDto.getIdDoctor());
        if (!doctor.isPresent()) {
            throw new RuntimeException("No existe un doctor con ID: " + reseniaInsertDto.getIdDoctor());
        }
        
        Usuario doctorEntity = doctor.get();
        
        // Verificar que es realmente un doctor
        if (doctorEntity.getTipoUsuario() != TipoUsuarioEnum.DOCTOR) {
            throw new RuntimeException("Solo se pueden crear reseñas para usuarios de tipo DOCTOR");
        }
        
        // Verificar que el doctor esté activo
        if (doctorEntity.getEstado() == null || !doctorEntity.getEstado()) {
            throw new RuntimeException("No se puede crear una reseña para un doctor inactivo");
        }
        
        // Validar calificación
        if (reseniaInsertDto.getCalificacion() == null) {
            throw new RuntimeException("La calificación es obligatoria");
        }
        
        if (!validarCalificacion(reseniaInsertDto.getCalificacion())) {
            throw new RuntimeException("La calificación debe estar entre 0.0 y 5.0 con máximo un decimal");
        }
        
        // Validar comentario si se proporciona
        if (reseniaInsertDto.getComentario() != null && !reseniaInsertDto.getComentario().trim().isEmpty()) {
            if (!validarComentario(reseniaInsertDto.getComentario())) {
                throw new RuntimeException("El comentario contiene contenido inapropiado o no válido");
            }
            
            if (reseniaInsertDto.getComentario().trim().length() < 5) {
                throw new RuntimeException("El comentario debe tener al menos 5 caracteres");
            }
        }
        
        // Verificar que no existan múltiples reseñas del mismo paciente al mismo doctor en un período corto
        // (Esta validación dependería del contexto de autenticación para obtener el ID del paciente actual)
        // Por ahora, verificamos reseñas muy similares en fechas cercanas
        /*List<Resenia> reseniasSimilares = reseniaRepository.findAll().stream()
                .filter(r -> r.getDoctor() != null && 
                        r.getDoctor().getIdUsuario().equals(reseniaInsertDto.getIdDoctor()) &&
                        r.getFechaResenia() != null &&
                        r.getFechaResenia().isAfter(LocalDate.now().minusDays(7)))
                .collect(Collectors.toList());
        
        // Verificar reseñas duplicadas con misma calificación y comentario similar
        boolean reseniasPosiblementeDuplicadas = reseniasSimilares.stream()
                .anyMatch(r -> r.getCalificacion() != null && 
                        r.getCalificacion().equals(reseniaInsertDto.getCalificacion()) &&
                        r.getComentario() != null && reseniaInsertDto.getComentario() != null &&
                        sonComentariosSimilares(r.getComentario(), reseniaInsertDto.getComentario()));
        
        if (reseniasPosiblementeDuplicadas) {
            System.out.println("ADVERTENCIA: Posible reseña duplicada detectada para doctor ID: " + reseniaInsertDto.getIdDoctor());
        }*/
        
        Resenia resenia = modelMapper.map(reseniaInsertDto, Resenia.class);
        resenia.setDoctor(doctorEntity);
        
        // Establecer fecha actual si no se proporciona
        if (resenia.getFechaResenia() == null) {
            resenia.setFechaResenia(LocalDate.now());
        }
        
        // Validar que la fecha no sea futura
        if (resenia.getFechaResenia().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de la reseña no puede ser futura");
        }
        
        // Normalizar comentario si existe
        if (resenia.getComentario() != null && !resenia.getComentario().trim().isEmpty()) {
            resenia.setComentario(normalizarComentario(resenia.getComentario()));
        }
        
        // Redondear calificación a un decimal
        resenia.setCalificacion(resenia.getCalificacion());
        
        Resenia savedResenia = reseniaRepository.save(resenia);
        
        // Actualizar estadísticas del doctor (esto se podría hacer de manera asíncrona)
        actualizarEstadisticasDoctor(doctorEntity.getIdUsuario());
        
        return mapToResponseDto(savedResenia);
    }

    @Override
    public ReseniaResponseDto update(Integer id, ReseniaUpdateDto reseniaUpdateDto) {
        Optional<Resenia> existingResenia = reseniaRepository.findById(id);
        
        if (existingResenia.isPresent()) {
            Resenia resenia = existingResenia.get();
            
            // Verificar que la reseña no sea muy antigua (regla de negocio para modificaciones)
            if (resenia.getFechaResenia() != null && 
                resenia.getFechaResenia().isBefore(LocalDate.now().minusDays(7))) {
                throw new RuntimeException("No se puede modificar una reseña con más de 7 días de antigüedad");
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (reseniaUpdateDto.getCalificacion() != null) {
                if (!validarCalificacion(reseniaUpdateDto.getCalificacion())) {
                    throw new RuntimeException("La nueva calificación debe estar entre 0.0 y 5.0 con máximo un decimal");
                }

                Double calificacionAnterior = resenia.getCalificacion();
                resenia.setCalificacion(reseniaUpdateDto.getCalificacion());
                
                // Log del cambio para auditoría
                System.out.println("AUDITORÍA: Reseña ID " + id + " - Calificación cambiada de " + 
                        calificacionAnterior + " a " + resenia.getCalificacion());
            }
            
            if (reseniaUpdateDto.getComentario() != null) {
                if (!reseniaUpdateDto.getComentario().trim().isEmpty()) {
                    if (!validarComentario(reseniaUpdateDto.getComentario())) {
                        throw new RuntimeException("El nuevo comentario contiene contenido inapropiado o no válido");
                    }
                    
                    if (reseniaUpdateDto.getComentario().trim().length() < 5) {
                        throw new RuntimeException("El comentario debe tener al menos 5 caracteres");
                    }
                    
                    String comentarioAnterior = resenia.getComentario();
                    resenia.setComentario(normalizarComentario(reseniaUpdateDto.getComentario()));
                    
                    // Log del cambio para auditoría
                    System.out.println("AUDITORÍA: Reseña ID " + id + " - Comentario modificado. Anterior: " + 
                            (comentarioAnterior != null ? comentarioAnterior.substring(0, Math.min(30, comentarioAnterior.length())) + "..." : "null"));
                } else {
                    // Vaciar comentario si se envía cadena vacía
                    resenia.setComentario(null);
                }
            }
            
            Resenia updatedResenia = reseniaRepository.save(resenia);
            
            // Actualizar estadísticas del doctor después de la modificación
            if (resenia.getDoctor() != null) {
                actualizarEstadisticasDoctor(resenia.getDoctor().getIdUsuario());
            }
            
            return mapToResponseDto(updatedResenia);
        }
        
        throw new RuntimeException("Reseña no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Resenia> resenia = reseniaRepository.findById(id);
        
        if (resenia.isPresent()) {
            Resenia reseniaEntity = resenia.get();
            
            // Verificar que la reseña no sea muy antigua (regla de negocio)
            if (reseniaEntity.getFechaResenia() != null && 
                reseniaEntity.getFechaResenia().isBefore(LocalDate.now().minusDays(3))) {
                throw new RuntimeException("No se puede eliminar una reseña con más de 3 días de antigüedad");
            }
            
            // Log de eliminación para auditoría
            Integer idDoctor = reseniaEntity.getDoctor() != null ? reseniaEntity.getDoctor().getIdUsuario() : null;
            System.out.println("AUDITORÍA: Eliminando reseña ID " + id + " - Doctor ID: " + idDoctor + 
                    " - Calificación: " + reseniaEntity.getCalificacion() + 
                    " - Fecha: " + reseniaEntity.getFechaResenia());
            
            reseniaRepository.deleteById(id);
            
            // Actualizar estadísticas del doctor después de la eliminación
            if (idDoctor != null) {
                actualizarEstadisticasDoctor(idDoctor);
            }
            
            return true;
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de reseñas
    private boolean validarCalificacion(Double calificacion) {
        if (calificacion == null) return false;
        
        // Verificar rango (0.0 a 5.0)
        Double min = 0.0;
        Double max = 5.0;
        
        if (calificacion.compareTo(min) < 0 || calificacion.compareTo(max) > 0) {
            return false;
        }
        
        // Verificar que tenga máximo un decimal
        return calificacion <= 1;
    }
    
    private boolean validarComentario(String comentario) {
        if (comentario == null || comentario.trim().isEmpty()) {
            return true; // Comentario vacío es válido
        }
        
        String comm = comentario.toLowerCase().trim();
        
        // Verificar términos inapropiados
        String[] terminosProhibidos = {
            "maldito", "estúpido", "idiota", "incompetente", "mierda", 
            "basura", "horrible", "terrible", "pésimo", "awful", "shit"
        };
        
        boolean contieneTerminoProhibido = Arrays.stream(terminosProhibidos)
                .anyMatch(comm::contains);
        
        // Verificar que no sea solo caracteres especiales o números
        boolean soloCaracteresEspeciales = !comm.matches(".*[a-záéíóúñ].*");
        
        return !contieneTerminoProhibido && !soloCaracteresEspeciales;
    }
    
    private String normalizarComentario(String comentario) {
        if (comentario == null) return null;
        
        // Limpiar espacios múltiples y normalizar formato
        String normalizado = comentario.trim().replaceAll("\\s+", " ");
        
        // Capitalizar primera letra
        if (!normalizado.isEmpty()) {
            normalizado = normalizado.substring(0, 1).toUpperCase() + 
                         normalizado.substring(1);
        }
        
        // Asegurar que termine con punto si es una oración completa
        if (normalizado.length() > 10 && !normalizado.endsWith(".") && 
            !normalizado.endsWith("!") && !normalizado.endsWith("?")) {
            normalizado += ".";
        }
        
        return normalizado;
    }
    
    private boolean sonComentariosSimilares(String comentario1, String comentario2) {
        if (comentario1 == null || comentario2 == null) return false;
        
        String c1 = comentario1.toLowerCase().replaceAll("[^a-záéíóúñ\\s]", "").trim();
        String c2 = comentario2.toLowerCase().replaceAll("[^a-záéíóúñ\\s]", "").trim();
        
        // Calcular similitud básica (esto se podría mejorar con algoritmos más sofisticados)
        return c1.equals(c2) || (c1.length() > 10 && c2.length() > 10 && 
                Math.abs(c1.length() - c2.length()) < 5 && c1.contains(c2.substring(0, Math.min(10, c2.length()))));
    }
    
    public Double getPromedioCalificacionDoctor(Integer idDoctor) {
        List<Resenia> resenias = reseniaRepository.findAll().stream()
                .filter(r -> r.getDoctor() != null && 
                        r.getDoctor().getIdUsuario().equals(idDoctor) &&
                        r.getCalificacion() != null)
                .collect(Collectors.toList());
        
        if (resenias.isEmpty()) {
            return 0.0;
        }

        Double suma = resenias.stream()
                .map(Resenia::getCalificacion)
                .reduce(0.0, Double::sum);
        
        // Solución: Usar división simple de Double
        return suma / resenias.size();
    }
    
    public int getNumeroReseniasPorDoctor(Integer idDoctor) {
        return (int) reseniaRepository.findAll().stream()
                .filter(r -> r.getDoctor() != null && 
                        r.getDoctor().getIdUsuario().equals(idDoctor))
                .count();
    }
    
    public List<Resenia> getReseniasRecientes(int dias) {
        LocalDate fechaLimite = LocalDate.now().minusDays(dias);
        return reseniaRepository.findAll().stream()
                .filter(r -> r.getFechaResenia() != null && 
                        !r.getFechaResenia().isBefore(fechaLimite))
                .collect(Collectors.toList());
    }
    
    public List<Resenia> getTopReseniasPorCalificacion(int limite) {
        return reseniaRepository.findAll().stream()
                .filter(r -> r.getCalificacion() != null)
                .sorted((r1, r2) -> r2.getCalificacion().compareTo(r1.getCalificacion()))
                .limit(limite)
                .collect(Collectors.toList());
    }
    
    private void actualizarEstadisticasDoctor(Integer idDoctor) {
        // Este método podría actualizar estadísticas agregadas del doctor
        // en una tabla separada o cachear los promedios
        Double promedio = getPromedioCalificacionDoctor(idDoctor);
        int numeroResenias = getNumeroReseniasPorDoctor(idDoctor);
        
        System.out.println("ESTADÍSTICAS ACTUALIZADAS - Doctor ID: " + idDoctor + 
                " - Promedio: " + promedio + " - Total reseñas: " + numeroResenias);
        
        // Aquí se podrían actualizar campos agregados en la entidad Usuario o crear una tabla de estadísticas
    }

    // Método helper para mapear a ResponseDto con información adicional
    private ReseniaResponseDto mapToResponseDto(Resenia resenia) {
        ReseniaResponseDto responseDto = modelMapper.map(resenia, ReseniaResponseDto.class);
        
        // Agregar información adicional del doctor
        /*if (resenia.getDoctor() != null) {
            responseDto.setIdDoctor(resenia.getDoctor().getIdUsuario());
            
            if (resenia.getDoctor().getPersona() != null) {
                String nombreDoctor = resenia.getDoctor().getPersona().getNombres() + 
                        " " + resenia.getDoctor().getPersona().getApellidos();
                responseDto.setNombreDoctor(nombreDoctor);
            }
            
            // Agregar estadísticas del doctor
            responseDto.setPromedioDoctor(getPromedioCalificacionDoctor(resenia.getDoctor().getIdUsuario()));
            responseDto.setTotalReseniasDroctor(getNumeroReseniasPorDoctor(resenia.getDoctor().getIdUsuario()));
        }
        
        // Categorizar la calificación
        if (resenia.getCalificacion() != null) {
            responseDto.setCategoriaCalificacion(categorizarCalificacion(resenia.getCalificacion()));
        }
        
        // Analizar sentimiento del comentario (básico)
        if (resenia.getComentario() != null && !resenia.getComentario().trim().isEmpty()) {
            responseDto.setSentimientoComentario(analizarSentimiento(resenia.getComentario()));
        }*/
        
        return responseDto;
    }
    
    private String categorizarCalificacion(BigDecimal calificacion) {
        if (calificacion.compareTo(new BigDecimal("4.5")) >= 0) {
            return "EXCELENTE";
        } else if (calificacion.compareTo(new BigDecimal("3.5")) >= 0) {
            return "BUENO";
        } else if (calificacion.compareTo(new BigDecimal("2.5")) >= 0) {
            return "REGULAR";
        } else if (calificacion.compareTo(new BigDecimal("1.5")) >= 0) {
            return "MALO";
        } else {
            return "MUY_MALO";
        }
    }
}