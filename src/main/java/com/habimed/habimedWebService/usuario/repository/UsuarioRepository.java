package com.habimed.habimedWebService.usuario.repository;

import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioRepository extends JpaRepository<Usuario, Integer> {


}