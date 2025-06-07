package com.habimed.habimedWebService.tipoUsuario.domain.service;

import java.util.List;

import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;

public interface TipoUsuarioService {
    /**
     * Retrieves all tipos de usuario
     * @return List of all tipos de usuario
     */
    List<TipoUsuario> getAllTipoUsuarioList();
}
