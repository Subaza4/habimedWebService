package com.habimed.habimedWebService.permisoHistorial.repository;

import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PermisoHistorialRepository {

    private JdbcTemplate jdbcTemplate;
    private PermisoHistorialDTO dto = new PermisoHistorialDTO();

    @Autowired
    public PermisoHistorialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void getPermisoHistorial(String sql) {

    }

    public void setPermisoHistorial(String sql) {}

}
