package com.habimed.habimedWebService.usuario.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    //dniPersona,tipoUsuario,usuario,contrasenia,token
    private Integer idUsuario;
    private Long dniPersona;
    private TipoUsuarioEnum tipoUsuario;
    private String usuario;
    private String contrasenia;
    private String token;
    private Boolean estado;
}
