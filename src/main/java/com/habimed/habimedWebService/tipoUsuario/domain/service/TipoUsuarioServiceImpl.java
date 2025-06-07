package com.habimed.habimedWebService.tipoUsuario.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;
import com.habimed.habimedWebService.tipoUsuario.repository.TipoUsuarioRepository;

@Service
public class TipoUsuarioServiceImpl implements TipoUsuarioService {
    
    private final TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    public TipoUsuarioServiceImpl(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public List<TipoUsuario> getAllTipoUsuarioList(){
        return this.tipoUsuarioRepository.findAll();
    }
}
