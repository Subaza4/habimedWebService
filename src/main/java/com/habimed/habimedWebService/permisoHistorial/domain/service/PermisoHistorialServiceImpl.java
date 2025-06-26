package com.habimed.habimedWebService.permisoHistorial.domain.service;

import com.habimed.habimedWebService.permisoHistorial.domain.model.EstadoPermisosEnum;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistoriaUpdateDto;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialFilterDto;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialResponseDto;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialnsertDto;
import com.habimed.habimedWebService.permisoHistorial.repository.PermisoHistorialRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.habimed.habimedWebService.permisoHistorial.domain.model.PermisosHistorial;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class PermisoHistorialServiceImpl implements PermisoHistorialService {
    
    private final PermisoHistorialRepository permisoHistorialRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PermisosHistorial> findAll() {
        return permisoHistorialRepository.findAll();
    }

    @Override
    public List<PermisosHistorial> findAllWithConditions(PermisoHistorialFilterDto permisoHistorialFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<PermisosHistorial> permisos = permisoHistorialRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (permisoHistorialFilterDto.getIdpermisohistorial() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getIdPermisoHistorial().equals(permisoHistorialFilterDto.getIdpermisohistorial()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getIddoctor() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getDoctor() != null && 
                            p.getDoctor().getIdUsuario().equals(permisoHistorialFilterDto.getIddoctor()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getIdpaciente() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getPaciente() != null && 
                            p.getPaciente().getIdUsuario().equals(permisoHistorialFilterDto.getIdpaciente()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getFechaotorgapermiso() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getFechaOtorgaPermiso() != null && 
                            p.getFechaOtorgaPermiso().equals(permisoHistorialFilterDto.getFechaotorgapermiso()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getFechadeniegapermiso() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getFechaDenegaPermiso() != null && 
                            p.getFechaDenegaPermiso().equals(permisoHistorialFilterDto.getFechadeniegapermiso()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getEstado() != null) {
            EstadoPermisosEnum estadoBuscado = permisoHistorialFilterDto.getEstado() ?
                    EstadoPermisosEnum.ACTIVO : EstadoPermisosEnum.INACTIVO;
            permisos = permisos.stream()
                    .filter(p -> p.getEstado() != null && p.getEstado().equals(estadoBuscado))
                    .collect(Collectors.toList());
        }
        
        return permisos;
    }

    @Override
    public PermisoHistorialResponseDto getById(Integer id) {
        Optional<PermisosHistorial> permiso = permisoHistorialRepository.findById(id);
        if (permiso.isPresent()) {
            return mapToResponseDto(permiso.get());
        }
        throw new RuntimeException("Permiso de historial no encontrado con ID: " + id);
    }

    @Override
    public PermisoHistorialResponseDto save(PermisoHistorialnsertDto permisoHistorialInsertDto) {
        // Validaciones específicas del contexto de permisos de historial
        if (permisoHistorialInsertDto.getIddoctor() == null) {
            throw new RuntimeException("El ID del doctor es obligatorio");
        }
        
        if (permisoHistorialInsertDto.getIdpaciente() == null) {
            throw new RuntimeException("El ID del paciente es obligatorio");
        }
        
        // Verificar que el doctor y paciente existen
        Optional<Usuario> doctor = usuarioRepository.findById(permisoHistorialInsertDto.getIddoctor());
        if (!doctor.isPresent()) {
            throw new RuntimeException("No existe un doctor con ID: " + permisoHistorialInsertDto.getIddoctor());
        }
        
        Optional<Usuario> paciente = usuarioRepository.findById(permisoHistorialInsertDto.getIdpaciente());
        if (!paciente.isPresent()) {
            throw new RuntimeException("No existe un paciente con ID: " + permisoHistorialInsertDto.getIdpaciente());
        }
        
        // Verificar que el doctor sea realmente un doctor y el paciente sea realmente un paciente
        if (doctor.get().getTipoUsuario() != TipoUsuarioEnum.DOCTOR) {
            throw new RuntimeException("El usuario con ID " + permisoHistorialInsertDto.getIddoctor() + " no es un doctor");
        }
        
        if (paciente.get().getTipoUsuario() != TipoUsuarioEnum.PACIENTE) {
            throw new RuntimeException("El usuario con ID " + permisoHistorialInsertDto.getIdpaciente() + " no es un paciente");
        }
        
        // Verificar que un paciente no se otorgue permisos a sí mismo
        if (permisoHistorialInsertDto.getIddoctor().equals(permisoHistorialInsertDto.getIdpaciente())) {
            throw new RuntimeException("Un usuario no puede otorgarse permisos de historial a sí mismo");
        }
        
        // Verificar si ya existe un permiso activo entre este doctor y paciente
        List<PermisosHistorial> permisosExistentes = permisoHistorialRepository.findAll();
        boolean permisoActivoExists = permisosExistentes.stream()
                .anyMatch(p -> p.getDoctor() != null && p.getPaciente() != null &&
                        p.getDoctor().getIdUsuario().equals(permisoHistorialInsertDto.getIddoctor()) &&
                        p.getPaciente().getIdUsuario().equals(permisoHistorialInsertDto.getIdpaciente()) &&
                        p.getEstado() == EstadoPermisosEnum.ACTIVO &&
                        p.getFechaDenegaPermiso() == null);
        
        if (permisoActivoExists) {
            throw new RuntimeException("Ya existe un permiso activo entre el doctor ID " + 
                    permisoHistorialInsertDto.getIddoctor() + " y el paciente ID " + 
                    permisoHistorialInsertDto.getIdpaciente());
        }
        
        PermisosHistorial permiso = modelMapper.map(permisoHistorialInsertDto, PermisosHistorial.class);
        permiso.setDoctor(doctor.get());
        permiso.setPaciente(paciente.get());
        
        // Establecer fecha de otorgamiento si no se proporciona
        if (permiso.getFechaOtorgaPermiso() == null) {
            permiso.setFechaOtorgaPermiso(LocalDate.now());
        }
        
        // Establecer estado por defecto como ACTIVO
        if (permiso.getEstado() == null) {
            permiso.setEstado(EstadoPermisosEnum.ACTIVO);
        }
        
        // Validar que la fecha de otorgamiento no sea futura
        if (permiso.getFechaOtorgaPermiso().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de otorgamiento no puede ser futura");
        }
        
        PermisosHistorial savedPermiso = permisoHistorialRepository.save(permiso);
        return mapToResponseDto(savedPermiso);
    }

    @Override
    public PermisoHistorialResponseDto update(Integer id, PermisoHistoriaUpdateDto permisoHistoriaUpdateDto) {
        Optional<PermisosHistorial> existingPermiso = permisoHistorialRepository.findById(id);
        
        if (existingPermiso.isPresent()) {
            PermisosHistorial permiso = existingPermiso.get();
            
            // Validar que el permiso no esté ya denegado
            if (permiso.getEstado() == EstadoPermisosEnum.INACTIVO && permiso.getFechaDenegaPermiso() != null) {
                throw new RuntimeException("No se puede modificar un permiso que ya ha sido denegado");
            }
            
            // Solo se puede actualizar el estado para denegar el permiso
            if (permisoHistoriaUpdateDto.getEstado() != null) {
                EstadoPermisosEnum nuevoEstado = permisoHistoriaUpdateDto.getEstado() ? 
                        EstadoPermisosEnum.ACTIVO : EstadoPermisosEnum.INACTIVO;
                
                // Si se está denegando el permiso, establecer fecha de denegación
                if (nuevoEstado == EstadoPermisosEnum.INACTIVO && permiso.getEstado() == EstadoPermisosEnum.ACTIVO) {
                    permiso.setFechaDenegaPermiso(LocalDate.now());
                }
                
                // Si se está reactivando un permiso, limpiar fecha de denegación
                if (nuevoEstado == EstadoPermisosEnum.ACTIVO && permiso.getEstado() == EstadoPermisosEnum.INACTIVO) {
                    permiso.setFechaDenegaPermiso(null);
                }
                
                permiso.setEstado(nuevoEstado);
            }
            
            // No permitir cambios en doctor, paciente o fecha de otorgamiento una vez creado
            if (permisoHistoriaUpdateDto.getFechadeniegapermiso() != null) {
                // Solo actualizar si se está denegando manualmente
                if (permiso.getEstado() == EstadoPermisosEnum.INACTIVO) {
                    if (permisoHistoriaUpdateDto.getFechadeniegapermiso().isBefore(permiso.getFechaOtorgaPermiso())) {
                        throw new RuntimeException("La fecha de denegación no puede ser anterior a la fecha de otorgamiento");
                    }
                    permiso.setFechaDenegaPermiso(permisoHistoriaUpdateDto.getFechadeniegapermiso());
                }
            }
            
            PermisosHistorial updatedPermiso = permisoHistorialRepository.save(permiso);
            return mapToResponseDto(updatedPermiso);
        }
        
        throw new RuntimeException("Permiso de historial no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<PermisosHistorial> permiso = permisoHistorialRepository.findById(id);
        
        if (permiso.isPresent()) {
            PermisosHistorial permisoEntity = permiso.get();
            
            // En lugar de eliminar físicamente, marcar como inactivo (soft delete)
            if (permisoEntity.getEstado() == EstadoPermisosEnum.ACTIVO) {
                permisoEntity.setEstado(EstadoPermisosEnum.INACTIVO);
                permisoEntity.setFechaDenegaPermiso(LocalDate.now());
                permisoHistorialRepository.save(permisoEntity);
                return true;
            }
            
            // Solo permitir eliminación física de permisos ya inactivos con más de 30 días
            if (permisoEntity.getFechaDenegaPermiso() != null && 
                permisoEntity.getFechaDenegaPermiso().isBefore(LocalDate.now().minusDays(30))) {
                permisoHistorialRepository.deleteById(id);
                return true;
            } else {
                throw new RuntimeException("Solo se pueden eliminar permisos inactivos con más de 30 días de antigüedad");
            }
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de permisos
    public boolean tienePermisoActivo(Integer idDoctor, Integer idPaciente) {
        List<PermisosHistorial> permisos = permisoHistorialRepository.findAll();
        return permisos.stream()
                .anyMatch(p -> p.getDoctor() != null && p.getPaciente() != null &&
                        p.getDoctor().getIdUsuario().equals(idDoctor) &&
                        p.getPaciente().getIdUsuario().equals(idPaciente) &&
                        p.getEstado() == EstadoPermisosEnum.ACTIVO &&
                        p.getFechaDenegaPermiso() == null);
    }
    
    public List<PermisosHistorial> getPermisosActivosPorDoctor(Integer idDoctor) {
        return permisoHistorialRepository.findAll().stream()
                .filter(p -> p.getDoctor() != null &&
                        p.getDoctor().getIdUsuario().equals(idDoctor) &&
                        p.getEstado() == EstadoPermisosEnum.ACTIVO &&
                        p.getFechaDenegaPermiso() == null)
                .collect(Collectors.toList());
    }
    
    public List<PermisosHistorial> getPermisosOtorgadosPorPaciente(Integer idPaciente) {
        return permisoHistorialRepository.findAll().stream()
                .filter(p -> p.getPaciente() != null &&
                        p.getPaciente().getIdUsuario().equals(idPaciente))
                .collect(Collectors.toList());
    }

    // Método helper para mapear a ResponseDto con información adicional
    private PermisoHistorialResponseDto mapToResponseDto(PermisosHistorial permiso) {
        PermisoHistorialResponseDto responseDto = modelMapper.map(permiso, PermisoHistorialResponseDto.class);
        
        // Agregar información adicional del doctor y paciente
        if (permiso.getDoctor() != null) {
            responseDto.setIddoctor(permiso.getDoctor().getIdUsuario());
            
            /*if (permiso.getDoctor().getPersona() != null) {
                String nombreDoctor = permiso.getDoctor().getPersona().getNombres() + 
                        " " + permiso.getDoctor().getPersona().getApellidos();
                responseDto.setNombreDoctor(nombreDoctor);
            }*/
        }
        
        if (permiso.getPaciente() != null) {
            responseDto.setIdpaciente(permiso.getPaciente().getIdUsuario());
            
            /*if (permiso.getPaciente().getPersona() != null) {
                String nombrePaciente = permiso.getPaciente().getPersona().getNombres() + 
                        " " + permiso.getPaciente().getPersona().getApellidos();
                responseDto.setNombrePaciente(nombrePaciente);
            }*/
        }
        
        // Convertir enum a boolean para compatibilidad
        responseDto.setEstado(permiso.getEstado() == EstadoPermisosEnum.ACTIVO);
        
        return responseDto;
    }
}