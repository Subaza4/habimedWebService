package com.habimed.habimedWebService.servicio.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.servicio.domain.parameter.request.ServicioRequest;
import com.habimed.habimedWebService.servicio.domain.parameter.response.ServicioResponse;
import com.habimed.habimedWebService.servicio.repository.dto.ServicioDTO;

@Repository
public class ServicioRepository {
    
    private final JdbcTemplate jdbcTemplate;

    private ServicioDTO dto = new ServicioDTO();

    @Autowired
    public ServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ServicioResponse> getAllServicios(ServicioRequest request) {
        String sql = "SELECT s.idservicio, s.nombre, s.descripcion, s.riesgos, s.idespecialidad, e.nombre AS especialidad_nombre, e.descripcion AS especialidad_descripcion "
                   + "FROM medic.\"servicio\" s "
                   + "JOIN medic.\"especialidad\" e ON s.idespecialidad = e.idespecialidad";
        return jdbcTemplate.query(sql, dto.getServicioRowMapper());
    }

    public Integer setServicio(ServicioRequest request) {
        String sql = "INSERT INTO medic.\"servicio\" (nombre, descripcion, riesgos, idespecialidad) VALUES (?, ?, ?, ?) RETURNING idservicio";
        return jdbcTemplate.queryForObject(sql, Integer.class, request.getNombre(), request.getDescripcion(), request.getRiesgos(), request.getIdespecialidad());
    }

    public boolean deleteServicio(Integer idservicio) {
        String sql = "DELETE FROM medic.\"servicio\" WHERE idservicio = ?";
        int rowsAffected = jdbcTemplate.update(sql, idservicio);
        return rowsAffected > 0;
    }
}
