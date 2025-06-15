package com.habimed.habimedWebService.usuario.domain.service;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.UsuarioFilterDto;
import com.habimed.habimedWebService.usuario.dto.UsuarioInsertDto;
import com.habimed.habimedWebService.usuario.dto.UsuarioResponseDto;
import com.habimed.habimedWebService.usuario.dto.UsuarioUpdateDto;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final ModelMapper modelMapper;
    // Opcional: Para encriptar contraseñas
    // private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> findAllWithConditions(UsuarioFilterDto usuarioFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (usuarioFilterDto.getDniPersona() != null) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getPersona() != null && 
                            u.getPersona().getDni().equals(usuarioFilterDto.getDniPersona()))
                    .collect(Collectors.toList());
        }
        
        if (usuarioFilterDto.getTipoUsuarioId() != null) {
            // Convertir ID a enum (asumiendo que 1=ADMIN, 2=DOCTOR, 3=PACIENTE, etc.)
            TipoUsuarioEnum tipoUsuario = convertirIdATipoUsuario(usuarioFilterDto.getTipoUsuarioId());
            if (tipoUsuario != null) {
                usuarios = usuarios.stream()
                        .filter(u -> u.getTipoUsuario() == tipoUsuario)
                        .collect(Collectors.toList());
            }
        }
        
        if (usuarioFilterDto.getUsuario() != null && !usuarioFilterDto.getUsuario().trim().isEmpty()) {
            String usuarioBuscado = usuarioFilterDto.getUsuario().toLowerCase().trim();
            usuarios = usuarios.stream()
                    .filter(u -> u.getCorreo() != null && 
                            u.getCorreo().toLowerCase().contains(usuarioBuscado))
                    .collect(Collectors.toList());
        }
        
        if (usuarioFilterDto.getEstado() != null) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getEstado() != null && u.getEstado().equals(usuarioFilterDto.getEstado()))
                    .collect(Collectors.toList());
        }
        
        return usuarios;
    }

    @Override
    public UsuarioResponseDto getById(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return mapToResponseDto(usuario.get());
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
    }

    @Override
    public UsuarioResponseDto save(UsuarioInsertDto usuarioInsertDto) {
//        // Validaciones específicas del contexto de Usuario
//        if (usuarioInsertDto.getDniPersona() == null) {
//            throw new RuntimeException("El DNI de la persona es obligatorio para crear un usuario");
//        }
//
        // Verificar que la persona existe
        Optional<Persona> persona = personaRepository.findById(usuarioInsertDto.getDniPersona());
        if (!persona.isPresent()) {
            throw new RuntimeException("No existe una persona con DNI: " + usuarioInsertDto.getDniPersona());
        }

        Persona personaEntity = persona.get();

        // Verificar que la persona no tenga ya un usuario del mismo tipo
        Boolean tipoUsuarioExiste = usuarioRepository.findAll().stream()
                .filter(u -> u.getPersona() != null &&
                        u.getPersona().getDni().equals(usuarioInsertDto.getDniPersona()))
                .collect(Collectors.toList()).stream().anyMatch(u -> u.getTipoUsuario() == usuarioInsertDto.getTipoUsuario());

        if (tipoUsuarioExiste){
            throw new RuntimeException("La persona ya tiene un usuario de tipo: " + usuarioInsertDto.getTipoUsuario());
        }

//        TipoUsuarioEnum tipoUsuarioNuevo = convertirIdATipoUsuario(usuarioInsertDto.getTipoUsuarioId());
//        if (tipoUsuarioNuevo == null) {
//            throw new RuntimeException("Tipo de usuario no válido: " + usuarioInsertDto.getTipoUsuarioId());
//        }
//
//        boolean tipoUsuarioExiste = usuariosPersona.stream()
//                .anyMatch(u -> u.getTipoUsuario() == tipoUsuarioNuevo);
//
//        if (tipoUsuarioExiste) {
//            throw new RuntimeException("La persona ya tiene un usuario de tipo: " + tipoUsuarioNuevo);
//        }
//
//        // Validar correo electrónico
//        if (usuarioInsertDto.getUsuario() == null || usuarioInsertDto.getUsuario().trim().isEmpty()) {
//            throw new RuntimeException("El correo electrónico es obligatorio");
//        }
//
//        if (!validarEmail(usuarioInsertDto.getUsuario())) {
//            throw new RuntimeException("El formato del correo electrónico no es válido");
//        }
        
        // Verificar que el correo no esté ya registrado
        Boolean emailExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getCorreo() != null && 
                        u.getCorreo().toLowerCase().equals(usuarioInsertDto.getCorreo().toLowerCase().trim()));
        
        if (emailExiste) {
            throw new RuntimeException("Ya existe un usuario registrado con el correo: " + 
                    usuarioInsertDto.getCorreo().trim());
        }
        
//        // Validar contraseña
//        if (usuarioInsertDto.getContrasenia() == null || usuarioInsertDto.getContrasenia().trim().isEmpty()) {
//            throw new RuntimeException("La contraseña es obligatoria");
//        }
//
//        if (!validarContrasenia(usuarioInsertDto.getContrasenia())) {
//            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres, " +
//                    "incluir mayúsculas, minúsculas, números y caracteres especiales");
//        }
//
//        // Validaciones específicas por tipo de usuario
//        validarTipoUsuarioEspecifico(tipoUsuarioNuevo, personaEntity);

        System.out.println("correo: " + usuarioInsertDto.getCorreo() +
                            " DniPersona: " + usuarioInsertDto.getDniPersona());

        Usuario usuario = modelMapper.map(usuarioInsertDto, Usuario.class);
        usuario.setPersona(personaEntity);

        System.out.println(usuario.getPersona());

//        usuario.setTipoUsuario(tipoUsuarioNuevo);
//        usuario.setCorreo(usuarioInsertDto.getUsuario().toLowerCase().trim());
        
//        // Encriptar contraseña (si tienes PasswordEncoder configurado)
//        // usuario.setContrasenia(passwordEncoder.encode(usuarioInsertDto.getContrasenia()));
//        usuario.setContrasenia(usuarioInsertDto.getContrasenia()); // Temporal sin encriptar
        
        // Establecer estado por defecto
        usuario.setEstado(usuarioInsertDto.getEstado() != null ? usuarioInsertDto.getEstado() : false);
        
        Usuario savedUsuario = usuarioRepository.save(usuario);

        return mapToResponseDto(savedUsuario);
    }

    @Override
    public UsuarioResponseDto update(Integer id, UsuarioUpdateDto usuarioUpdateDto) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        
        if (existingUsuario.isPresent()) {
            Usuario usuario = existingUsuario.get();
            
            // Verificar si el usuario tiene citas o reseñas asociadas para restricciones
            boolean tieneActividad = verificarActividadUsuario(usuario);
            
            // Actualizar solo los campos que no son null en el DTO
            if (usuarioUpdateDto.getUsuario() != null && !usuarioUpdateDto.getUsuario().trim().isEmpty()) {
                if (!validarEmail(usuarioUpdateDto.getUsuario())) {
                    throw new RuntimeException("El formato del correo electrónico no es válido");
                }
                
                // Verificar que el nuevo correo no esté ya registrado por otro usuario
                boolean emailExiste = usuarioRepository.findAll().stream()
                        .anyMatch(u -> !u.getIdUsuario().equals(id) && 
                                u.getCorreo() != null && 
                                u.getCorreo().toLowerCase().equals(usuarioUpdateDto.getUsuario().toLowerCase().trim()));
                
                if (emailExiste) {
                    throw new RuntimeException("Ya existe otro usuario registrado con el correo: " + 
                            usuarioUpdateDto.getUsuario().trim());
                }
                
                usuario.setCorreo(usuarioUpdateDto.getUsuario().toLowerCase().trim());
            }
            
            if (usuarioUpdateDto.getContrasenia() != null && !usuarioUpdateDto.getContrasenia().trim().isEmpty()) {
                if (!validarContrasenia(usuarioUpdateDto.getContrasenia())) {
                    throw new RuntimeException("La nueva contraseña debe tener al menos 8 caracteres, " +
                            "incluir mayúsculas, minúsculas, números y caracteres especiales");
                }
                
                // Encriptar nueva contraseña
                // usuario.setContrasenia(passwordEncoder.encode(usuarioUpdateDto.getContrasenia()));
                usuario.setContrasenia(usuarioUpdateDto.getContrasenia()); // Temporal sin encriptar
                
                // Log de cambio de contraseña
                System.out.println("AUDITORÍA: Contraseña cambiada para usuario ID: " + id + 
                        " - Correo: " + usuario.getCorreo());
            }
            
            if (usuarioUpdateDto.getEstado() != null) {
                // Verificar restricciones para desactivar usuarios
                if (!usuarioUpdateDto.getEstado() && tieneActividad) {
                    throw new RuntimeException("No se puede desactivar un usuario que tiene citas o actividad pendiente");
                }
                
                usuario.setEstado(usuarioUpdateDto.getEstado());
                
                // Log de cambio de estado
                System.out.println("AUDITORÍA: Estado cambiado para usuario ID: " + id + 
                        " - Nuevo estado: " + usuarioUpdateDto.getEstado());
            }
            
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return mapToResponseDto(updatedUsuario);
        }
        
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        
        if (usuario.isPresent()) {
            Usuario usuarioEntity = usuario.get();
            
            // Verificar que el usuario no tenga actividad que impida la eliminación
            if (verificarActividadUsuario(usuarioEntity)) {
                throw new RuntimeException("No se puede eliminar un usuario que tiene citas, reseñas o actividad registrada. " +
                        "Considere desactivar el usuario en lugar de eliminarlo.");
            }
            
            // Verificar que no sea el último administrador
            if (usuarioEntity.getTipoUsuario() == TipoUsuarioEnum.ADMIN) {
                long administradoresActivos = usuarioRepository.findAll().stream()
                        .filter(u -> u.getTipoUsuario() == TipoUsuarioEnum.ADMIN && 
                                u.getEstado() != null && u.getEstado())
                        .count();
                
                if (administradoresActivos <= 1) {
                    throw new RuntimeException("No se puede eliminar el último administrador activo del sistema");
                }
            }
            
            // Log de eliminación para auditoría
            System.out.println("AUDITORÍA: Eliminando usuario ID " + id + 
                    " - Tipo: " + usuarioEntity.getTipoUsuario() + 
                    " - Correo: " + usuarioEntity.getCorreo() + 
                    " - DNI Persona: " + (usuarioEntity.getPersona() != null ? 
                     usuarioEntity.getPersona().getDni() : "null"));
            
            usuarioRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // Métodos helper específicos para el contexto de usuarios
    private TipoUsuarioEnum convertirIdATipoUsuario(Integer id) {
        // Mapeo de IDs a tipos de usuario (ajustar según tu implementación)
        switch (id) {
            case 1: return TipoUsuarioEnum.ADMIN;
            case 2: return TipoUsuarioEnum.DOCTOR;
            case 3: return TipoUsuarioEnum.PACIENTE;
            default: return null;
        }
    }
    
    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email.trim()).matches();
    }
    
    private boolean validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 8) {
            return false;
        }
        
        // Al menos una mayúscula, una minúscula, un número y un carácter especial
        boolean tieneMayuscula = contrasenia.matches(".*[A-Z].*");
        boolean tieneMinuscula = contrasenia.matches(".*[a-z].*");
        boolean tieneNumero = contrasenia.matches(".*[0-9].*");
        boolean tieneEspecial = contrasenia.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }
    
    private void validarTipoUsuarioEspecifico(TipoUsuarioEnum tipoUsuario, Persona persona) {
        switch (tipoUsuario) {
            case DOCTOR:
                // Los doctores deben ser mayores de edad y tener cierta edad mínima
                if (persona.getFechaNacimiento() != null) {
                    int edad = java.time.Period.between(persona.getFechaNacimiento(), 
                            java.time.LocalDate.now()).getYears();
                    if (edad < 25) {
                        throw new RuntimeException("Un doctor debe tener al menos 25 años de edad");
                    }
                }
                break;
                
            case PACIENTE:
                // Los pacientes deben ser mayores de edad o tener un responsable
                if (persona.getFechaNacimiento() != null) {
                    int edad = java.time.Period.between(persona.getFechaNacimiento(), 
                            java.time.LocalDate.now()).getYears();
                    if (edad < 18) {
                        // Podría requerir información del responsable legal
                        System.out.println("ADVERTENCIA: Paciente menor de edad registrado - DNI: " + 
                                persona.getDni());
                    }
                }
                break;
                
            case ADMIN:
                // Los administradores deben ser mayores de edad
                if (persona.getFechaNacimiento() != null) {
                    int edad = java.time.Period.between(persona.getFechaNacimiento(), 
                            java.time.LocalDate.now()).getYears();
                    if (edad < 21) {
                        throw new RuntimeException("Un administrador debe tener al menos 21 años de edad");
                    }
                }
                break;
        }
    }
    
    private boolean verificarActividadUsuario(Usuario usuario) {
        // Esta verificación dependería de tener acceso a los repositorios de citas, reseñas, etc.
        // Por ahora, una implementación básica
        
        if (usuario.getTipoUsuario() == TipoUsuarioEnum.DOCTOR) {
            // Verificar si tiene citas como doctor o reseñas
            return (usuario.getCitasComoDoctor() != null && !usuario.getCitasComoDoctor().isEmpty()) ||
                   (usuario.getHorarios() != null && !usuario.getHorarios().isEmpty());
        }
        
        if (usuario.getTipoUsuario() == TipoUsuarioEnum.PACIENTE) {
            // Verificar si tiene citas como paciente
            return usuario.getCitasComoPaciente() != null && !usuario.getCitasComoPaciente().isEmpty();
        }
        
        return false;
    }

    // Método helper para mapear a ResponseDto
    private UsuarioResponseDto mapToResponseDto(Usuario usuario) {
        UsuarioResponseDto responseDto = modelMapper.map(usuario, UsuarioResponseDto.class);
        
        // Agregar información adicional de la persona
        if (usuario.getPersona() != null) {
            responseDto.setDniPersona(usuario.getPersona().getDni());
            // El ModelMapper debería mapear automáticamente la persona completa
        }
        
        // No incluir la contraseña en la respuesta por seguridad
        // La contraseña ya no está en el UsuarioResponseDto
        
        // Agregar estadísticas del usuario según el tipo
        /*if (usuario.getTipoUsuario() == TipoUsuarioEnum.DOCTOR) {
            // Agregar estadísticas del doctor (número de citas, calificación promedio, etc.)
            if (usuario.getCitasComoDoctor() != null) {
                responseDto.setNumeroCitasComoDoctor(usuario.getCitasComoDoctor().size());
            }
        }
        
        if (usuario.getTipoUsuario() == TipoUsuarioEnum.PACIENTE) {
            // Agregar estadísticas del paciente
            if (usuario.getCitasComoPaciente() != null) {
                responseDto.setNumeroCitasComoPaciente(usuario.getCitasComoPaciente().size());
            }
        }*/
        
        return responseDto;
    }
}