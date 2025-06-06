package com.habimed.habimedWebService.tipoUsuario.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;
import com.habimed.habimedWebService.tipoUsuario.dto.TipoUsuarioDTO;

@Repository
public class TipoUsuarioRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TipoUsuarioDTO dto = new TipoUsuarioDTO();
    
    @Autowired
    public TipoUsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find all personas
    public List<TipoUsuario> findAll() {
        String sql = "SELECT * FROM medic.\"tipousuario\"";
        return jdbcTemplate.query(sql, dto.tipoUsuarioRowMapper());
    }

    public Integer existTipoUsuario(Integer id) {
        String sql = "SELECT 1 FROM medic.\"tipousuario\" WHERE \"id\" = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> dto.existeUsuarioMapper(rs), id);
    }
}