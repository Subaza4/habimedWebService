package com.habimed.habimedWebService.usuario.dto;

import java.util.Map;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest extends RequestREST {
    private Long dniPersona;
    private Integer idTipoUsuario;
    private String usuario;
    private String contrasenia;
    private Boolean estado;

    public String buildConditions() {
        Map<String, String> conditions = getValuesOfConditions();
        StringBuilder conditionsString = new StringBuilder();
        Boolean isFirst = true;
        
        for (String key : conditions.keySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                conditionsString.append(" AND ");
            }

            // Campos de texto que usan LIKE
            if (key.equalsIgnoreCase("usuario")) {
                conditionsString.append(key).append(" LIKE '%").append(conditions.get(key)).append("%'");
            } 
            // Campos numéricos o booleanos que usan =
            else {
                conditionsString.append(key).append(" = ").append(conditions.get(key));
            }
        }
        return !conditionsString.isEmpty() ? " WHERE " + conditionsString.toString() : "";
    }

    public Map<String, String> getValuesOfConditions() {
        Map<String, String> conditions = new java.util.HashMap<>();
        
        if (dniPersona != null && dniPersona > 0)
            conditions.put("dnipersona", String.valueOf(dniPersona));
        if (idTipoUsuario != null && idTipoUsuario > 0)
            conditions.put("idtipousuario", String.valueOf(idTipoUsuario));
        if (usuario != null && !usuario.isEmpty())
            conditions.put("usuario", usuario);
        // La contraseña normalmente no se usa en consultas de búsqueda
        // if (contrasenia != null && !contrasenia.isEmpty())
        //     conditions.put("contrasenia", contrasenia);
        conditions.put("estado", String.valueOf(estado));

        return conditions;
    }

    public String getColumnasSelect(String alias) {
        StringBuilder columnas = new StringBuilder();
        columnas.append(alias).append("dnipersona as usuario_dnipersona, ");
        columnas.append(alias).append("idtipousuario as usuario_idtipousuario, ");
        columnas.append(alias).append("usuario as usuario_usuario, ");
        // No incluimos la contraseña en las consultas SELECT por seguridad
        columnas.append(alias).append("estado as usuario_estado ");
        return columnas.toString();
    }
}