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

    public void setPermisoHistorial(String sql) {

    }

    public PermisoHistorialDTO getpermisoDoctor(Integer idDoctor, Integer idPaciente) {
        String sql = "select p.* from medic.\"permisos_historial\" p where p.iddoctor = ? AND p.idpaciente = ?";

        return jdbcTemplate.query(sql, dto.permisoHistorialRowMapper(), idDoctor, idPaciente).get(0);
    }

}
