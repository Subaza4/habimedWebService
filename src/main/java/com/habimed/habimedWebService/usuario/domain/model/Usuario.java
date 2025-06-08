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
    @NotNull
    private TipoUsuario tipoUsuario;
    @NotBlank(message = "El usuario no puede estar en blanco")
    private String usuario;
    @NotBlank(message = "La contraseña no puede estar en blanco")
    private String contrasenia;
    @NotNull
    private String token;
}
