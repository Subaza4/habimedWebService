package com.habimed.habimedWebService.tipoUsuario.repository.dto;

import org.springframework.jdbc.core.RowMapper;

import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;

public class TipoUsuarioDTO {
    // Row mapper for Product
    public RowMapper<TipoUsuario> tipoUsuarioRowMapper() {
        return (rs, rowNum) -> {
            TipoUsuario tipo_usuario = new TipoUsuario();
            tipo_usuario.setId(rs.getInt("id"));
            tipo_usuario.setNombre(rs.getString("nombre"));
            tipo_usuario.setDescripcion(rs.getString("descripcion"));
            return tipo_usuario;
        };
    }

    public int existeUsuarioMapper(java.sql.ResultSet rs) throws java.sql.SQLException {
        return rs.getInt(1);
    }
}
