package com.habimed.habimedWebService.usuario.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.tipoUsuario.repository.TipoUsuarioRepository;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PersonaRepository personaRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioResponseDto> findAllUsuarios(UsuarioFilterDto request) {
        List<UsuarioResponseDto> respuesta = new ArrayList<>();
        //primero traemos la lista de usuarios
        List<Usuario> usuarios = this.usuarioRepository.findAllUsuarios(request);
        //traemos el detalle de cada persona de forma independiente
        for(Usuario usuario : usuarios){
            Optional<Persona> _persona = personaRepository.findById(usuario.getDniPersona());
            if(_persona.isPresent()){
                Persona persona = _persona.get();
                UsuarioResponseDto res = new UsuarioResponseDto();
                res.setIdUsuario(usuario.getIdUsuario());
                res.setDniPersona(persona.getDni());
                res.setTipoUsuario(usuario.getTipoUsuario());
                res.setContrasenia(usuario.getContrasenia());
                res.setEstado(usuario.getEstado());
                res.setPersona(persona);
            }
        }

        return respuesta;
    }

    @Override
    public UsuarioResponseDto findByIdUsuario(Integer id) {
        UsuarioResponseDto respuesta = new UsuarioResponseDto();
        Optional<Usuario> usuario = this.usuarioRepository.findByIdUsuario(id);
        if(usuario.isPresent()){
            Optional<Persona> _persona = personaRepository.findById(usuario.get().getDniPersona()));
            if(_persona.isPresent()){
                Persona persona = _persona.get();
                respuesta.setIdUsuario(usuario.get().getIdUsuario());
                respuesta.setDniPersona(persona.getDni());
                respuesta.setTipoUsuario(usuario.get().getTipoUsuario());
                respuesta.setContrasenia(usuario.get().getContrasenia());
                respuesta.setEstado(usuario.get().getEstado());
                respuesta.setPersona(persona);
            }
        }else{
            throw new RuntimeException("Usuario no encontrado");
        }
        return respuesta;
    }

    /*
             1. **Códigos de retorno**:
                - 1: Inserción exitosa
                - 2: Actualización exitosa
                - 3: Usuario no existe
                - 4: Parámetros nulos
                - 5: DNI nulo
                - 6: DNI no existe
                - 7: Tipo usuario no existe
             */
    @Override
    public UsuarioResponseDto saveUsuario(UsuarioInsertDto request){
        UsuarioResponseDto respuesta = new UsuarioResponseDto();
        //Primero validar si el tipo de usuario existe
        Optional<Persona> _persona = personaRepository.findById(request.getDniPersona());
        if(_persona.isPresent()) {
            Usuario _usuario = new Usuario();

            Usuario usuario = this.usuarioRepository.setUsuario(_usuario);
            if(usuario == null){
                throw new RuntimeException("No se pudo crear el usuario");
            }
            respuesta.setIdUsuario(usuario.getIdUsuario());
            respuesta.setDniPersona(usuario.getDniPersona());
            respuesta.setTipoUsuario(usuario.getTipoUsuario());
            respuesta.setContrasenia(usuario.getContrasenia());
            respuesta.setEstado(usuario.getEstado());
            respuesta.setPersona(_persona.get());
        }else{
            throw new RuntimeException("No existe un registro de la persona con el DNI: " + request.getDniPersona());
        }

        return respuesta;
    }

    @Override
    public Boolean deleteUsuario(Integer idusuario) {
        //return this.usuarioRepository.deleteUsuario(dni);
        return false;
    }

    @Override
    public UsuarioDTO loginUser (LoginRequest request){
        if(request.getUsuario() == null || request.getContrasenia() == null){
            return null;
        }
        UsuarioDTO respuesta = this.usuarioRepository.loginUser(request);
        return respuesta;
    }

    @Override
    public boolean logoutUser (String token){
        Boolean respuesta = false;
        if(token != null && !token.equals("")){
            respuesta = this.usuarioRepository.logoutUser(token);
        }
        return respuesta;
    }

    @Override
    public Boolean validateToken(String token){
        Boolean respuesta = false;
        if(token != null && !token.equals("")){
            respuesta = this.usuarioRepository.validateToken(token);
        }
        return respuesta;
    }

    @Override
    public UsuarioDTO getUsuarioByToken(String token){
        return this.usuarioRepository.getUsuarioByToken(token);
    }

    @Override
    public UsuarioDTO registrarUsuarioIndp(NewUsuarioRequest request){
        UsuarioDTO respuesta = new UsuarioDTO();
        if(request.getPersona() != null || request.getUsuario() != null){

        }
        return respuesta;
    }
}
