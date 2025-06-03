package com.habimed.habimedWebService.usuario.domain.service;

import java.util.List;

import com.habimed.habimedWebService.usuario.domain.parameter.request.UsuarioRequest;
import com.habimed.habimedWebService.usuario.repository.dto.UsuarioDTO;

public interface UsuarioService {
    /**
     * Retorna la lista de usuarios filtrados por parametros (tipo, nombre, correo, estado)
     * @return
     */
    List<UsuarioDTO> getListUsuarios(UsuarioRequest request);

    /**
     * Obtener los datos un usuario por su id o username
    */
    // UsuarioDTO getUsuario();

    /**
     * Actualizar los datos de un usuario
    */
    Integer setUsuario(UsuarioRequest usuario);


    /**
     * Eliminar un usuario por su dni
    */
    boolean deleteUsuario(String dni);

}
