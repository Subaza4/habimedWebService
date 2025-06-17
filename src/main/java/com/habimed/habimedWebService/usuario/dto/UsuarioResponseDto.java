package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.persona.dto.PersonaResponseDto;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Integer idUsuario;
    private Long dniPersona;
    private PersonaResponseDto persona;
    private TipoUsuarioEnum tipoUsuario;
    private String correo;
    private Boolean estado;
}