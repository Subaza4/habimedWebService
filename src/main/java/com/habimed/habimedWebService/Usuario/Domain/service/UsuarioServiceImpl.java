package com.habimed.habimedWebService.usuario.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.tipoUsuario.repository.TipoUsuarioRepository;
import com.habimed.habimedWebService.usuario.domain.parameter.request.UsuarioRequest;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import com.habimed.habimedWebService.usuario.repository.dto.UsuarioDTO;

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

    /* @Override
    public UsuarioDTO getUsuario(String dni) {
        return this.usuarioRepository.getUsuario(dni);
    } */

    @Override
    public Integer setUsuario(UsuarioRequest request){
        //Primero validar si el tipo de usuario existe
        Integer valid = tipoUsuarioRepository.existTipoUsuario(request.getIdTipoUsuario());
        if(valid == 1){
            return 0; // Tipo de usuario no existe
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
}
