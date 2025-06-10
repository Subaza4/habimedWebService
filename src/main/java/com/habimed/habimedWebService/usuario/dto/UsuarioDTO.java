package com.habimed.habimedWebService.usuario.dto;

import java.util.Map;

import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;
import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Integer idusuario;
    private Long dniPersona;
    private Integer idTipoUsuario;
    private String nombreTipoUsuario;
    private String usuario;
    private String contrasenia;
    private String token;
    private boolean estado;
    private TipoUsuario tipoUsuario;
    

    public RowMapper<UsuarioDTO> usuarioRowMapper() {
        return (rs, rowNum) -> {
            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setIdusuario(rs.getInt("idusuario"));
            usuario.setDniPersona(rs.getLong("dnipersona"));
            usuario.setNombreTipoUsuario(rs.getString("nombreusuario"));
            usuario.setUsuario(nombreTipoUsuario);;
            return usuario;
        };
    }


    public String buildCondition(Map<String, String> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" WHERE ");
        boolean first = true;
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            if (!first) {
                sb.append(" AND ");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            if ("dniPersona".equalsIgnoreCase(key) || "usuario".equalsIgnoreCase(key) || "contrasenia".equalsIgnoreCase(key)) {
                sb.append(key).append(" LIKE '%").append(value).append("%'");
            } else {
                sb.append(key).append(" = '").append(value).append("'");
            }
            first = false;
        }
        return sb.toString();
    }
}
