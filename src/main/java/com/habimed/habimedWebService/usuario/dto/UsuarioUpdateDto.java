package com.habimed.habimedWebService.usuario.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UsuarioUpdateDto {
    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuario;

    @Size(max = 255, message = "La contraseña no puede exceder 255 caracteres")
    private String contrasenia;

    @Size(max = 255, message = "El token no puede exceder 255 caracteres")
    private String token;

    private Boolean estado;
}