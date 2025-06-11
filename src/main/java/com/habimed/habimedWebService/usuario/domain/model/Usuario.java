package com.habimed.habimedWebService.usuario.domain.model;

import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    //dniPersona,tipoUsuario,usuario,contrasenia,token
    private Long dniPersona;
    private TipoUsuario tipoUsuario;
    private String usuario;
    private String contrasenia;
    private String token;
}
