package com.habimed.habimedWebService.usuario.repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.dto.UsuarioRequest;

@Repository
public class UsuarioRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UsuarioDTO dto = new UsuarioDTO();

    @Autowired
    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Obtener un usuario o varios usuarios
    public List<UsuarioDTO> getListUsuarios(UsuarioRequest request) {
        String aliasPersona = "pe.";
        String aliasUsuario = "us.";
        String aliaStringsTipoUsuario = "tu.";
        StringBuilder query = new StringBuilder();
        query.append("SELECT ").append(aliasPersona).append("\"dnipersona\" dni, ")
             .append(aliasPersona).append("\"nombres\" , ").append(aliasPersona).append("\"apellidos\" , ")
             .append(" FROM medic.\"usuario\" ").append(aliasUsuario)
             .append(" INNER JOIN medic.\"persona\" ").append(aliasPersona)
             .append(" ON ").append(aliasUsuario).append(".\"dnipersona\" = ").append(aliasPersona).append(".\"dnipersona\" ")
             .append(" INNER JOIN medic.\"tipousuario\" ").append(aliaStringsTipoUsuario)
             .append(" ON ").append(aliasUsuario).append(".\"tipousuarioId\" = ")
             .append(aliaStringsTipoUsuario).append(".\"id\" ");
        if(!request.getValuesOfConditions().isEmpty()){
            String condition = "";
            condition = dto.buildCondition(request.getValuesOfConditions());
            System.out.println("condiciones "  + condition);
            query.append(condition);
        }
        
        return jdbcTemplate.query(query.toString(), dto.usuarioRowMapper());
    }

    // Find usuario
    /* public UsuarioDTO getUsuario(String dni) {
        String sql = "SELECT * FROM medic.\"usuario\" us INNER JOIN medic.\"tipousuario\" tu ON us.\"tipousuarioid\" = tu.\"id\" WHERE us.\"dnipersona\" = ?";
        return jdbcTemplate.query(sql, dto.usuarioRowMapper(), dni);
    } */

    // Save usuario usando un stored procedure y obteniendo un parámetro de salida
    public Integer setUsuario(UsuarioRequest usuario) {
        return jdbcTemplate.execute(
            (CallableStatementCreator) connection -> {
                CallableStatement cs = connection.prepareCall("{call mi_procedimiento(?, ?, ?, ?)}");
                cs.setString(1, usuario.getDniPersona().toString());
                cs.setInt(2, usuario.getIdTipoUsuario());
                cs.setString(3, usuario.getContrasenia());
                cs.registerOutParameter(4, Types.INTEGER);
                return cs;
            },
            (CallableStatementCallback<Integer>) cs -> {
                cs.execute();
                return cs.getInt(4); // Retorna el valor del parámetro de salida (entero)
            }
        );
    }
}
