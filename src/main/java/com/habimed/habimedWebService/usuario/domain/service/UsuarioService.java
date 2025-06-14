package com.habimed.habimedWebService.usuario.domain.service;

import java.util.List;

import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.*;

public interface UsuarioService {
    List<Usuario> findAll();
    List<Usuario> findAllWithConditions(UsuarioFilterDto usuarioFilterDto);
    UsuarioResponseDto getById(Integer id);
    UsuarioResponseDto save(UsuarioInsertDto usuarioInsertDto);
    Boolean delete(Integer id);
    UsuarioResponseDto update(Integer id, UsuarioUpdateDto usuarioUpdateDto);
}