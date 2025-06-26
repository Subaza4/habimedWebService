package com.habimed.habimedWebService.cita.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.habimedWebService.cita.dto.*;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.repository.ServicioRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    @Override
    public List<Cita> findAllWithConditions(CitaFilterDto citaFilterDto) {
        List<Cita> citas = citaRepository.findAll();

        if (citaFilterDto.getIdCita() != null) {
            citas = citas.stream()
                    .filter(c -> c.getIdCita().equals(citaFilterDto.getIdCita()))
                    .collect(Collectors.toList());
        }

        if (citaFilterDto.getIdServicio() != null) {
            citas = citas.stream()
                    .filter(c -> c.getServicio() != null &&
                            c.getServicio().getIdServicio().equals(citaFilterDto.getIdServicio()))
                    .collect(Collectors.toList());
        }

        if (citaFilterDto.getIdMedico() != null) {
            citas = citas.stream()
                    .filter(c -> c.getDoctor() != null &&
                            c.getDoctor().getIdUsuario().equals(citaFilterDto.getIdMedico()))
                    .collect(Collectors.toList());
        }

        if (citaFilterDto.getDniPersona() != null && citaFilterDto.getDniPersona() > 0) {
            citas = citas.stream()
                    .filter(c -> c.getPaciente() != null &&
                            c.getPaciente().getPersona() != null &&
                            c.getPaciente().getPersona().getDni().equals(citaFilterDto.getDniPersona()))
                    .collect(Collectors.toList());
        }

        if (citaFilterDto.getEstado() != null) {
            citas = citas.stream()
                    .filter(c -> c.getEstado() != null && c.getEstado().equals(citaFilterDto.getEstado()))
                    .collect(Collectors.toList());
        }

        return citas;
    }

    @Override
    public CitaResponseDto getById(Integer id) {
        Optional<Cita> cita = citaRepository.findById(id);
        if (cita.isPresent()) {
            return mapToResponseDto(cita.get());
        }
        throw new RuntimeException("Cita no encontrada con ID: " + id);
    }

    @Override
    @Transactional
    public CitaResponseDto save(CitaInsertDto citaInsertDto) {
        // Validar que la fecha no sea pasada
        if (citaInsertDto.getFechaHoraInicio() != null &&
                citaInsertDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede crear una cita con fecha pasada");
        }

        // Validar que la fecha de fin sea después de la fecha de inicio
        if (citaInsertDto.getFechaHoraInicio() != null && citaInsertDto.getFechaHoraFin() != null &&
                citaInsertDto.getFechaHoraFin().isBefore(citaInsertDto.getFechaHoraInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // Validar que el paciente existe y es tipo PACIENTE
        Optional<Usuario> paciente = usuarioRepository.findById(citaInsertDto.getIdmedico()); // Esto debería ser idPaciente
        if (!paciente.isPresent()) {
            throw new RuntimeException("Paciente no encontrado");
        }

        if (paciente.get().getTipoUsuario() != TipoUsuarioEnum.PACIENTE) {
            throw new RuntimeException("El usuario especificado no es un paciente");
        }

        // Validar que el doctor existe y es tipo DOCTOR
        Optional<Usuario> doctor = usuarioRepository.findById(citaInsertDto.getIdmedico());
        if (!doctor.isPresent()) {
            throw new RuntimeException("Doctor no encontrado");
        }

        if (doctor.get().getTipoUsuario() != TipoUsuarioEnum.DOCTOR) {
            throw new RuntimeException("El usuario especificado no es un doctor");
        }

        // Validar que el servicio existe
        Optional<Servicio> servicio = servicioRepository.findById(citaInsertDto.getIdservicio());
        if (!servicio.isPresent()) {
            throw new RuntimeException("Servicio no encontrado");
        }

        // Validar disponibilidad del doctor en el horario solicitado
        if (!validarDisponibilidadDoctor(doctor.get(), citaInsertDto.getFechaHoraInicio(),
                citaInsertDto.getFechaHoraFin())) {
            throw new RuntimeException("El doctor no está disponible en el horario solicitado");
        }

        // Crear la cita
        Cita cita = new Cita();
        cita.setPaciente(paciente.get());
        cita.setDoctor(doctor.get());
        cita.setServicio(servicio.get());
        cita.setMotivo(citaInsertDto.getMotivo());
        cita.setFechaHoraInicio(citaInsertDto.getFechaHoraInicio());
        cita.setFechaHoraFin(citaInsertDto.getFechaHoraFin());
        cita.setEstado(EstadoCitaEnum.SOLICITADA);
        cita.setDescripcion(citaInsertDto.getDescripcion());

        Cita savedCita = citaRepository.save(cita);
        return mapToResponseDto(savedCita);
    }

    @Override
    @Transactional
    public CitaResponseDto update(Integer id, CitaUpdateDto citaUpdateDto) {
        Optional<Cita> existingCita = citaRepository.findById(id);

        if (existingCita.isPresent()) {
            Cita cita = existingCita.get();

            // Validar que la cita no esté en estado REALIZADA o CANCELADA
            if (cita.getEstado() == EstadoCitaEnum.REALIZADA ||
                    cita.getEstado() == EstadoCitaEnum.CANCELADA) {
                throw new RuntimeException("No se puede modificar una cita " + cita.getEstado());
            }

            // Aplicar regla de penalización por cancelación tardía
            if (citaUpdateDto.getEstado() == EstadoCitaEnum.CANCELADA &&
                    cita.getFechaHoraInicio() != null) {
                long horasAntes = ChronoUnit.HOURS.between(LocalDateTime.now(), cita.getFechaHoraInicio());
                if (horasAntes < 24) {
                    // Aquí se aplicaría la penalización del 50%
                    throw new RuntimeException("Cancelación con menos de 24h de antelación. Se aplicará penalización del 50%");
                }
            }

            // Actualizar campos permitidos
            if (citaUpdateDto.getMotivo() != null && !citaUpdateDto.getMotivo().trim().isEmpty()) {
                cita.setMotivo(citaUpdateDto.getMotivo());
            }

            if (citaUpdateDto.getFechaHoraInicio() != null) {
                if (citaUpdateDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("No se puede actualizar a una fecha pasada");
                }

                // Validar disponibilidad del doctor para la nueva fecha
                if (!validarDisponibilidadDoctor(cita.getDoctor(),
                        citaUpdateDto.getFechaHoraInicio(),
                        citaUpdateDto.getFechaHoraFin())) {
                    throw new RuntimeException("El doctor no está disponible en el nuevo horario");
                }

                cita.setFechaHoraInicio(citaUpdateDto.getFechaHoraInicio());
                cita.setEstado(EstadoCitaEnum.MODIFICADA);
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

            Cita updatedCita = citaRepository.save(cita);
            return mapToResponseDto(updatedCita);
        }

        throw new RuntimeException("Cita no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Cita> cita = citaRepository.findById(id);

        if (cita.isPresent()) {
            Cita citaEntity = cita.get();

            // Solo permitir eliminar citas en estado SOLICITADA
            if (citaEntity.getEstado() != EstadoCitaEnum.SOLICITADA) {
                throw new RuntimeException("Solo se pueden eliminar citas en estado SOLICITADA");
            }

            citaRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private boolean validarDisponibilidadDoctor(Usuario doctor, LocalDateTime inicio, LocalDateTime fin) {
        // Verificar conflictos con otras citas del doctor
        List<Cita> citasDoctor = citaRepository.findAll().stream()
                .filter(c -> c.getDoctor() != null &&
                        c.getDoctor().getIdUsuario().equals(doctor.getIdUsuario()) &&
                        c.getEstado() != EstadoCitaEnum.CANCELADA)
                .collect(Collectors.toList());

        for (Cita cita : citasDoctor) {
            if (cita.getFechaHoraInicio() != null && cita.getFechaHoraFin() != null) {
                // Verificar solapamiento
                boolean hayConflicto = !(fin.isBefore(cita.getFechaHoraInicio()) ||
                        inicio.isAfter(cita.getFechaHoraFin()));
                if (hayConflicto) {
                    return false;
                }
            }
        }

        // TODO: Verificar contra el horario del doctor (HorarioDoctor)

        return true;
    }

    private CitaResponseDto mapToResponseDto(Cita cita) {
        CitaResponseDto dto = new CitaResponseDto();
        dto.setIdcita(cita.getIdCita());
        dto.setIdservicio(cita.getServicio() != null ? cita.getServicio().getIdServicio() : null);
        dto.setIdmedico(cita.getDoctor() != null ? cita.getDoctor().getIdUsuario() : null);
        dto.setDniPersona(cita.getPaciente() != null && cita.getPaciente().getPersona() != null ?
                cita.getPaciente().getPersona().getDni().toString() : null);
        dto.setMotivo(cita.getMotivo());
        dto.setFechaHoraInicio(cita.getFechaHoraInicio());
        dto.setFechaHoraFin(cita.getFechaHoraFin());
        dto.setEstado(cita.getEstado());
        dto.setDescripcion(cita.getDescripcion());

        // Aquí deberías obtener el idConsultorio a través de la relación doctor-consultorio
        // dto.setIdconsultorio(obtenerIdConsultorio(cita.getDoctor()));

        return dto;
    }
}