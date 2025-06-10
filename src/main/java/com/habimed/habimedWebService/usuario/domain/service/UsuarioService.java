package com.habimed.habimedWebService.usuario.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.usuario.dto.LoginRequest;
import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.dto.UsuarioRequest;

public interface UsuarioService {
    /*
     *  Login de un usuario
     */
    UsuarioDTO loginUser(LoginRequest usuario);

    boolean logoutUser(String token);

    Boolean validateToken (String token);

    /**
     * Retorna la lista de usuarios filtrados por parametros (tipo, nombre, correo, estado)
     * @return
     */
    List<UsuarioDTO> getListUsuarios(UsuarioRequest request);

    /**
     * Obtener los datos un usuario por su id o username
    */
    UsuarioDTO getUsuario(Long dni);

    /**
     * Actualizar los datos de un usuario
    */
    Integer setUsuario(UsuarioRequest usuario);


    /**
     * Eliminar un usuario por su dni
    */
    boolean deleteUsuario(String dni);

    UsuarioDTO getUsuarioByToken(String token);
}
