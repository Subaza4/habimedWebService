package com.habimed.habimedWebService.usuario.domain.service;

import java.util.List;

import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.LoginRequest;
import com.habimed.habimedWebService.usuario.dto.NewUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.tipoUsuario.repository.TipoUsuarioRepository;
import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.dto.UsuarioRequest;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> getListUsuarios(UsuarioRequest request) {
        return this.usuarioRepository.getListUsuarios(request);
    }

    @Override
    public UsuarioDTO getUsuario(Long dni) {
        UsuarioDTO usuario = this.usuarioRepository.getUsuario(dni);
        usuario.setTipoUsuario(this.tipoUsuarioRepository.getDetalleTipoUsuario(usuario.getIdTipoUsuario()));
        return usuario;
    }

    @Override
    public Integer setUsuario(UsuarioRequest request){
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
        //Primero validar si el tipo de usuario existe
        Integer valid = tipoUsuarioRepository.existTipoUsuario(request.getIdTipoUsuario());
        if(valid == 1){
            return 7; // Tipo de usuario no existe
        }else{
            Integer respuesta = this.usuarioRepository.setUsuario(request);

            return respuesta;
        }
    }

    @Override
    public boolean deleteUsuario(String dni) {
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

    ////////////////////////////////////////////////////////////////////
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
