package com.habimed.habimedWebService.horarioDoctor.domain.service;

import com.habimed.habimedWebService.horarioDoctor.domain.model.DiaEnum;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import com.habimed.habimedWebService.horarioDoctor.dto.*;
import com.habimed.habimedWebService.horarioDoctor.repository.HorarioDoctorRepository;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;

import java.lang.constant.Constable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class HorarioDoctorServiceImpl implements HorarioDoctorService {
    
    private final HorarioDoctorRepository horarioDoctorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<HorarioDoctor> findAll() {
        return horarioDoctorRepository.findAll();
    }

    @Override
    public List<HorarioDoctor> findAllWithConditions(HorarioDoctorFilterDto horarioDoctorFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<HorarioDoctor> horarios = horarioDoctorRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (horarioDoctorFilterDto.getIdhorariodoctor() != null) {
            horarios = horarios.stream()
                    .filter(h -> h.getIdHorarioDoctor().equals(horarioDoctorFilterDto.getIdhorariodoctor()))
                    .collect(Collectors.toList());
        }
        
        if (horarioDoctorFilterDto.getIddoctor() != null) {
            horarios = horarios.stream()
                    .filter(h -> h.getDoctor() != null && 
                            h.getDoctor().getIdUsuario().equals(horarioDoctorFilterDto.getIddoctor()))
                    .collect(Collectors.toList());
        }
        
        if (horarioDoctorFilterDto != null && horarioDoctorFilterDto.getDiaSemana() != null) {
            // Convertir el String del filtro a DiaEnum usando tu método existente
            Object diaEnumBuscado = normalizarDiaSemana(horarioDoctorFilterDto.getDiaSemana());
            
            if (diaEnumBuscado instanceof DiaEnum) {
                DiaEnum diaBuscado = (DiaEnum) diaEnumBuscado;
                horarios = horarios.stream()
                        .filter(h -> h.getDiaSemana() != null && 
                                h.getDiaSemana().equals(diaBuscado))
                        .collect(Collectors.toList());
            }
        }
        
        if (horarioDoctorFilterDto.getHoraInicio() != null) {
            horarios = horarios.stream()
                    .filter(h -> h.getHoraInicio() != null && 
                            !h.getHoraInicio().toLocalTime().isBefore(horarioDoctorFilterDto.getHoraInicio()))
                    .collect(Collectors.toList());
        }
        
        if (horarioDoctorFilterDto.getHoraFin() != null) {
            horarios = horarios.stream()
                    .filter(h -> h.getHoraFin() != null && 
                            !h.getHoraFin().toLocalTime().isAfter(horarioDoctorFilterDto.getHoraFin()))
                    .collect(Collectors.toList());
        }
        
        return horarios;
    }

    @Override
    public HorarioDoctorResponseDto getById(Integer id) {
        Optional<HorarioDoctor> horarioDoctor = horarioDoctorRepository.findById(id);
        if (horarioDoctor.isPresent()) {
            return mapToResponseDto(horarioDoctor.get());
        }
        throw new RuntimeException("Horario de doctor no encontrado con ID: " + id);
    }

    @Override
    public HorarioDoctorResponseDto save(HorarioDoctorInsertDto horarioDoctorInsertDto) {
        // Validaciones específicas del contexto de horarios médicos
        if (horarioDoctorInsertDto.getIdDoctor() == null) {
            throw new RuntimeException("El ID del doctor es obligatorio");
        }
        
        // Verificar que el doctor existe
        Optional<Usuario> doctor = usuarioRepository.findById(horarioDoctorInsertDto.getIdDoctor());
        if (!doctor.isPresent()) {
            throw new RuntimeException("No existe un doctor con ID: " + horarioDoctorInsertDto.getIdDoctor());
        }
        
        // Validar día de la semana
        if (horarioDoctorInsertDto.getDiaSemana() != null) {
            if (!esDiaSemanaValido(horarioDoctorInsertDto.getDiaSemana())) {
                throw new RuntimeException("Día de la semana no válido: " + horarioDoctorInsertDto.getDiaSemana());
            }
        }
        
        // Validar horarios lógicos
        if (horarioDoctorInsertDto.getHoraInicio() != null && horarioDoctorInsertDto.getHoraFin() != null) {
            if (!horarioDoctorInsertDto.getHoraInicio().isBefore(horarioDoctorInsertDto.getHoraFin())) {
                throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
            }
            
            // Validar horarios de trabajo razonables (6:00 AM - 10:00 PM)
            LocalTime inicioLocalTime = horarioDoctorInsertDto.getHoraInicio().toLocalTime();
            LocalTime finLocalTime = horarioDoctorInsertDto.getHoraFin().toLocalTime();
            
            if (inicioLocalTime.isBefore(LocalTime.of(6, 0)) || finLocalTime.isAfter(LocalTime.of(22, 0))) {
                throw new RuntimeException("Los horarios deben estar entre 6:00 AM y 10:00 PM");
            }
        }
        
        if (horarioDoctorInsertDto.getDiaSemana() != null) {
            String diaBuscado = horarioDoctorInsertDto.getDiaSemana().toUpperCase().trim();
            
            List<HorarioDoctor> horariosExistentes = horarioDoctorRepository.findAll();
            boolean hayConflicto = horariosExistentes.stream()
                    .anyMatch(h -> h.getDoctor() != null && 
                            h.getDoctor().getIdUsuario().equals(horarioDoctorInsertDto.getIdDoctor()) &&
                            h.getDiaSemana() != null &&
                            h.getDiaSemana().name().equalsIgnoreCase(diaBuscado) &&
                            hayConflictoHorario(h, horarioDoctorInsertDto.getHoraInicio(), horarioDoctorInsertDto.getHoraFin()));
            
            if (hayConflicto) {
                throw new RuntimeException("Ya existe un horario conflictivo para el doctor en el día " + 
                        horarioDoctorInsertDto.getDiaSemana());
            }
        }
        
        // Calcular duración en minutos automáticamente
        if (horarioDoctorInsertDto.getHoraInicio() != null && horarioDoctorInsertDto.getHoraFin() != null) {
            long duracionMinutos = calcularDuracionMinutos(
                    horarioDoctorInsertDto.getHoraInicio(), 
                    horarioDoctorInsertDto.getHoraFin());
            horarioDoctorInsertDto.setDuracionMinutos((int) duracionMinutos);
        }
        
        HorarioDoctor horarioDoctor = modelMapper.map(horarioDoctorInsertDto, HorarioDoctor.class);
        horarioDoctor.setDoctor(doctor.get());
        
        // Normalizar día de la semana
        if (horarioDoctor.getDiaSemana() != null) {
            horarioDoctor.setDiaSemana(normalizarDiaSemana(String.valueOf(horarioDoctor.getDiaSemana())));
        }
        
        HorarioDoctor savedHorarioDoctor = horarioDoctorRepository.save(horarioDoctor);
        return mapToResponseDto(savedHorarioDoctor);
    }

    @Override
    public HorarioDoctorResponseDto update(Integer id, HorarioDoctorUpdateDto horarioDoctorUpdateDto) {
        Optional<HorarioDoctor> existingHorario = horarioDoctorRepository.findById(id);
        
        if (existingHorario.isPresent()) {
            HorarioDoctor horarioDoctor = existingHorario.get();
            
            // Validar día de la semana si se actualiza
            if (horarioDoctorUpdateDto.getDiaSemana() != null) {
                if (!esDiaSemanaValido(horarioDoctorUpdateDto.getDiaSemana())) {
                    throw new RuntimeException("Día de la semana no válido: " + horarioDoctorUpdateDto.getDiaSemana());
                }
                horarioDoctor.setDiaSemana(normalizarDiaSemana(horarioDoctorUpdateDto.getDiaSemana()));
            }
            
            // Actualizar horarios
            LocalDateTime nuevaHoraInicio = horarioDoctorUpdateDto.getHoraInicio() != null ?
                    horarioDoctorUpdateDto.getHoraInicio() : horarioDoctor.getHoraInicio();
            LocalDateTime nuevaHoraFin = horarioDoctorUpdateDto.getHoraFin() != null ?
                    horarioDoctorUpdateDto.getHoraFin() : horarioDoctor.getHoraFin();
            
            // Validar horarios lógicos
            if (nuevaHoraInicio != null && nuevaHoraFin != null) {
                if (!nuevaHoraInicio.isBefore(nuevaHoraFin)) {
                    throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin");
                }
                
                // Verificar conflictos de horarios
                String diaActual = horarioDoctorUpdateDto.getDiaSemana() != null ? 
                        horarioDoctorUpdateDto.getDiaSemana() : String.valueOf(horarioDoctor.getDiaSemana());
                
                if (diaActual != null && horarioDoctor.getDoctor() != null) {
                    List<HorarioDoctor> horariosExistentes = horarioDoctorRepository.findAll();
                    boolean hayConflicto = horariosExistentes.stream()
                            .anyMatch(h -> !h.getIdHorarioDoctor().equals(horarioDoctor.getIdHorarioDoctor()) &&
                                    h.getDoctor() != null && 
                                    h.getDoctor().getIdUsuario().equals(horarioDoctor.getDoctor().getIdUsuario()) &&
                                    h.getDiaSemana() != null &&
                                    h.getDiaSemana().name().equalsIgnoreCase(diaActual) &&
                                    hayConflictoHorario(h, nuevaHoraInicio, nuevaHoraFin));
                    
                    if (hayConflicto) {
                        throw new RuntimeException("El horario actualizado genera conflicto con otro horario existente");
                    }
                }
            }
            
            // Actualizar campos
            if (horarioDoctorUpdateDto.getHoraInicio() != null) {
                horarioDoctor.setHoraInicio(horarioDoctorUpdateDto.getHoraInicio());
            }
            if (horarioDoctorUpdateDto.getHoraFin() != null) {
                horarioDoctor.setHoraFin(horarioDoctorUpdateDto.getHoraFin());
            }
            
            // Recalcular duración
            /*if (horarioDoctor.getHoraInicio() != null && horarioDoctor.getHoraFin() != null) {
                long duracionMinutos = calcularDuracionMinutos(
                        horarioDoctor.getHoraInicio(), 
                        horarioDoctor.getHoraFin());
                horarioDoctor.setDuracionMinutos((int) duracionMinutos);
            }*/
            
            HorarioDoctor updatedHorarioDoctor = horarioDoctorRepository.save(horarioDoctor);
            return mapToResponseDto(updatedHorarioDoctor);
        }
        
        throw new RuntimeException("Horario de doctor no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<HorarioDoctor> horarioDoctor = horarioDoctorRepository.findById(id);
        
        if (horarioDoctor.isPresent()) {
            // En un sistema real, verificarías si hay citas programadas en este horario
            // Por ahora, permitimos la eliminación directa
            horarioDoctorRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Métodos helper
    private boolean esDiaSemanaValido(String diaSemana) {
        List<String> diasValidos = Arrays.asList(
                "lunes", "martes", "miércoles", "jueves", "viernes", "sábado", "domingo",
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
        );
        return diasValidos.contains(diaSemana.toLowerCase());
    }

    private DiaEnum normalizarDiaSemana(String diaSemana) {
        if (diaSemana == null) return null;
        
        String diaLower = diaSemana.toLowerCase().trim();
        
        switch (diaLower) {
            case "monday":
            case "lunes":
                return DiaEnum.LUNES;
            case "tuesday":
            case "martes":
                return DiaEnum.MARTES;
            case "wednesday":
            case "miércoles":
            case "miercoles":
                return DiaEnum.MIERCOLES;
            case "thursday":
            case "jueves":
                return DiaEnum.JUEVES;
            case "friday":
            case "viernes":
                return DiaEnum.VIERNES;
            case "saturday":
            case "sábado":
            case "sabado":
                return DiaEnum.SABADO;
            case "sunday":
            case "domingo":
                return DiaEnum.DOMINGO;
            default:
                throw new IllegalArgumentException("Día de la semana no válido: " + diaSemana);
        }
    }

    private boolean hayConflictoHorario(HorarioDoctor horarioExistente, LocalDateTime nuevaHoraInicio, LocalDateTime nuevaHoraFin) {
        if (horarioExistente.getHoraInicio() == null || horarioExistente.getHoraFin() == null ||
            nuevaHoraInicio == null || nuevaHoraFin == null) {
            return false;
        }
        
        // Verificar si hay solapamiento
        return !(nuevaHoraFin.isBefore(horarioExistente.getHoraInicio()) ||
                nuevaHoraInicio.isAfter(horarioExistente.getHoraFin()));
    }

    private long calcularDuracionMinutos(LocalDateTime horaInicio, LocalDateTime horaFin) {
        LocalTime inicio = horaInicio.toLocalTime();
        LocalTime fin = horaFin.toLocalTime();
        
        return java.time.Duration.between(inicio, fin).toMinutes();
    }

    private HorarioDoctorResponseDto mapToResponseDto(HorarioDoctor horarioDoctor) {
        HorarioDoctorResponseDto responseDto = modelMapper.map(horarioDoctor, HorarioDoctorResponseDto.class);
        
        // Agregar información adicional del doctor
        if (horarioDoctor.getDoctor() != null) {
            responseDto.setIdDoctor(horarioDoctor.getDoctor().getIdUsuario());
            
            if (horarioDoctor.getDoctor().getPersona() != null) {
                String nombreDoctor = horarioDoctor.getDoctor().getPersona().getNombres() + 
                        " " + horarioDoctor.getDoctor().getPersona().getApellidos();
                responseDto.setNombreDoctor(nombreDoctor);
            }
        }
        
        return responseDto;
    }
}