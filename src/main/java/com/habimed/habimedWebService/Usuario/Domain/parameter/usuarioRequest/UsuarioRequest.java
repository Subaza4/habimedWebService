package com.habimed.habimedWebService.usuario.domain.parameter.usuarioRequest;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    private Long dniPersona;
    private Integer idTipoUsuario;
    private String usuario;
    private String contrasenia;
    private boolean estado;

    public Map<String, String> getValuesOfConditions(){
        Map<String, String> conditions = new java.util.HashMap<>();
        if(dniPersona != null && dniPersona > 0)
            conditions.put("dnipersona", String.valueOf(dniPersona));
        if(idTipoUsuario != null && idTipoUsuario > 0)
            conditions.put("idtipousuario", String.valueOf(idTipoUsuario));
        if(usuario != null && !usuario.isEmpty())
            conditions.put("usuario", usuario);
        if(contrasenia != null && !contrasenia.isEmpty())
            conditions.put("contrasenia", contrasenia);
            
        conditions.put("estado", String.valueOf(estado));

        return conditions;
    }
}
