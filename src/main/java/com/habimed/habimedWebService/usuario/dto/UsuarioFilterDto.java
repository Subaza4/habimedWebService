package com.habimed.habimedWebService.usuario.dto;

import lombok.Data;

@Data
public class UsuarioFilterDto {
    private Long dniPersona;
    private Integer tipoUsuarioId;
    private String usuario;
    private Boolean estado;
}