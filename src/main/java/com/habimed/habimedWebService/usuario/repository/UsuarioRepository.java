package com.habimed.habimedWebService.usuario.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

import com.habimed.habimedWebService.usuario.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
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
    public UsuarioDTO getUsuario(Long dni) {
        String sql = "SELECT * FROM medic.\"usuario\" us WHERE us.\"dnipersona\" = ?";
        return jdbcTemplate.query(sql, dto.usuarioRowMapper(), dni).get(0);
    }

    // Save usuario usando un stored procedure y obteniendo un parámetro de salida
    public Integer setUsuario(UsuarioRequest usuario) {
        return jdbcTemplate.execute(
            (CallableStatementCreator) connection -> {
                CallableStatement cs = connection.prepareCall(
                    "{? = call medic.upsert_usuario(?, ?, ?, ?, ?, ?, ?)}");
                
                // Registrar el parámetro de retorno
                cs.registerOutParameter(1, Types.INTEGER);
                
                // Parámetros de entrada
                cs.setNull(2, Types.INTEGER);  // p_idusuario (null para inserción)
                cs.setLong(3, usuario.getDniPersona());  // p_dnipersona
                cs.setInt(4, usuario.getIdTipoUsuario());  // p_tipousuario
                cs.setString(5, usuario.getUsuario());  // p_usuario
                cs.setString(6, usuario.getContrasenia());  // p_contrasenia_plain
                cs.setBoolean(7, false);  // p_actualizar_contrasenia
                cs.setBoolean(8, true);  // p_estado
                
                return cs;
            },
            (CallableStatementCallback<Integer>) cs -> {
                cs.execute();
                return cs.getInt(1);  // Obtener el valor de retorno
            }
        );
    }

    public boolean deleteUsuario(Integer id){
        String sql = "DELETE FROM medic.\"usuario\" WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    public boolean logoutUser(String token) {
        String sql = "{call medic.logout_usuario(?)}";
        
        try {
            return Boolean.TRUE.equals(jdbcTemplate.execute(
                    (PreparedStatementCreator) connection -> {
                        CallableStatement cs = connection.prepareCall(sql);
                        cs.setString(1, token);
                        return cs;
                    },
                    cs -> {
                        cs.execute();
                        return true;
                    }
            ));
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean validateToken(String token) {
        String sql = "SELECT medic.validar_token_estado(?)";
        
        return jdbcTemplate.execute(
                (PreparedStatementCreator) connection -> {
                    CallableStatement cs = connection.prepareCall(sql);
                    cs.setString(1, token);
                    return cs;
                },
            cs -> {
                ResultSet rs = cs.executeQuery();
                return rs.next() ? rs.getBoolean(1) : false;
            }
        );
    }

    public UsuarioDTO loginUser(LoginRequest request) {
        String sql = "SELECT medic.verificar_credenciales(?, ?)";
        
        try {
            String token = jdbcTemplate.execute(
                    (PreparedStatementCreator) connection -> {
                        CallableStatement cs = connection.prepareCall(sql);
                        cs.setString(1, request.getUsuario());
                        cs.setString(2, request.getContrasenia());
                        return cs;
                    },
                cs -> {
                    ResultSet rs = cs.executeQuery();
                    return rs.next() ? rs.getString(1) : null;
                }
            );

            if (token == null || "1".equals(token) || "2".equals(token)) {
                return null;
            }

            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setUsuario(request.getUsuario());
            usuario.setToken(token);
            usuario.setEstado(true);
            return usuario;

        } catch (Exception e) {
            return null;
        }
    }
}