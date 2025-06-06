package com.habimed.habimedWebService.consultorioTieneServicio.repository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;

public class ConsultorioTieneServicioRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ConsultorioTieneServicioDTO dto = new ConsultorioTieneServicioDTO();

    @Autowired
    public ConsultorioTieneServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ConsultorioTieneServicioDTO> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request) {
        String sql = "SELECT idconsultoriotienesser, idconsultorio, idservicio FROM medic.\"consultoriotienesser\" ";
        sql += request.getValuesOfConditions();
        List<ConsultorioTieneServicioDTO> consultoriosServicios = jdbcTemplate.query(sql, dto.consultorioTieneServicioRowMapper());
        return consultoriosServicios;
    }

    public ConsultorioTieneServicioDTO getConsultorioServicioById(ConsultorioTieneServicioRequest request) {
        String sql = "SELECT idconsultoriotienesser, idconsultorio, idservicio FROM medic.\"consultoriotienesser\" ";
        sql += request.getValuesOfConditions();
        return jdbcTemplate.queryForObject(sql, dto.consultorioTieneServicioRowMapper());
    }

    public Integer setConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        String sql = "INSERT INTO medic.\"consultoriotienesser\" (idconsultorio, idservicio) VALUES (?, ?)";
        return jdbcTemplate.update(sql, request.getIdconsultorio(), request.getIdservicio());
    }

    public Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        String sql = "DELETE FROM medic.\"consultoriotienesser\" WHERE idconsultorio = ? AND idservicio = ?";
        int rowsAffected = jdbcTemplate.update(sql, request.getIdconsultorio(), request.getIdservicio());
        return rowsAffected > 0;
    }
}