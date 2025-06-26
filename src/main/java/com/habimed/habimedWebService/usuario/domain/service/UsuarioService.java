package com.habimed.habimedWebService.usuario.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.*;

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
    List<UsuarioResponseDto> findAllUsuarios(UsuarioFilterDto request);

    /**
     * Obtener los datos un usuario por su id o username
    */
    UsuarioResponseDto findByIdUsuario(Integer id);

    /**
     * Crear de un usuario
    */
    UsuarioResponseDto saveUsuario(UsuarioInsertDto usuario);

    /**
     * Actualizar los datos de un usuario
     * @param usuario
     * @return
     */
    UsuarioResponseDto updateUsuario(UsuarioInsertDto usuario);

    /**
     * Eliminar un usuario por su idusuario
    */
    Boolean deleteUsuario(Integer idusuario);

    UsuarioDTO getUsuarioByToken(String token);

    ////////////////////////////////////////////////////
    /// Método para que un nuevo usuario se registre
    UsuarioDTO registrarUsuarioIndp(NewUsuarioRequest request);

}
