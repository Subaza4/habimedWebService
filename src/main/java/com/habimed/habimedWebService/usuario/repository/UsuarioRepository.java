package com.habimed.habimedWebService.usuario.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.LoginRequest;
import com.habimed.habimedWebService.usuario.dto.UsuarioFilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.dto.UsuarioRequest;

@Repository
public class UsuarioRepository {

    @Value("${habimed.default.pagina}")
    private static Integer NumElementos;
    @Value("${habimed.default.numelementos}")
    private static Integer NumPagina;

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioDTO dto = new UsuarioDTO();

    @Autowired
    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Usuario> findAllUsuarios(UsuarioFilterDto request) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.dnipersona, u.tipousuario, u.usuario, u.estado, ");
        sql.append("tu.nombre as tipo_usuario_nombre ");
        sql.append("FROM medic.usuario u WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        request.buildWhereClause("u", sql, params);

        sql.append(" ORDER BY u.dnipersona DESC LIMIT ? OFFSET ?");
        Integer limit = request.getNum_elementos() > 0 ? request.getNum_elementos() : NumElementos;
        Integer offset = request.getPagina() > 0 ? (request.getPagina() -1) : NumPagina;
        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), usuarioRowMapper(), params.toArray());
    }

    public Optional<Usuario> findByIdUsuario(Integer id){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.dnipersona, u.tipousuario, u.usuario, u.estado, ");
        sql.append("tu.nombre as tipo_usuario_nombre ");
        sql.append("FROM medic.usuario u WHERE u.idusuario = ? ");
        try{
            return Optional.of(jdbcTemplate.queryForObject(sql.toString(), usuarioRowMapper(), id));
        }catch(Exception e){
            return Optional.empty();
        }
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
    public Integer setUsuario(Usuario usuario) {
        return jdbcTemplate.execute(
            (CallableStatementCreator) connection -> {
                CallableStatement cs = connection.prepareCall(
                    "{? = call medic.upsert_usuario(?, ?, ?, ?, ?, ?, ?)}");
                
                // Registrar el parámetro de retorno
                cs.registerOutParameter(1, Types.INTEGER);
                
                // Parámetros de entrada
                cs.setNull(2, Types.INTEGER);  // p_idusuario (null para inserción)
                cs.setLong(3, usuario.getDniPersona());  // p_dnipersona
                cs.setString(4, String.valueOf(usuario.getTipoUsuario()));  // p_tipousuario cambiar por un string
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
        String sql = "DELETE FROM medic.usuario WHERE id = ?";
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

    public UsuarioDTO getUsuarioByToken(String token) {
        String sql = "select u.* from medic.\"login\" l " +
                " INNER JOIN medic.\"usuario\" u on u.idusuario = l.idusuario" +
                "where l.estado=true AND l.token=? ";
        return jdbcTemplate.query(sql, dto.usuarioRowMapper(), token).get(0);

    }


    private RowMapper<Usuario> usuarioRowMapper() {
        return (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setDniPersona(rs.getLong("dnipersona"));
            usuario.setTipoUsuario(TipoUsuarioEnum.valueOf(rs.getString("tipousuario")));
            usuario.setUsuario(rs.getString("usuario"));
            // Por seguridad, no devolvemos la contraseña en consultas normales
            usuario.setEstado(rs.getBoolean("estado"));
            usuario.setContrasenia("***"); // Valor enmascarado

            return usuario;
        };
    }

}