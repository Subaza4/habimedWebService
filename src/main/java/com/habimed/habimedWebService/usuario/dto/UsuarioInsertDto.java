package com.habimed.habimedWebService.usuario.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UsuarioInsertDto {
    @NotNull(message = "El DNI de la persona es obligatorio")
    private Long dniPersona;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private Integer tipoUsuarioId;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 255, message = "La contraseña no puede exceder 255 caracteres")
    private String contrasenia;

    @Size(max = 255, message = "El token no puede exceder 255 caracteres")
    private String token;

    private Boolean estado = false;
}