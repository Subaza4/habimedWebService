package com.habimed.habimedWebService.consultorio.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.consultorio.domain.parameter.consultorioRequest.ConsultorioRequest;
import com.habimed.habimedWebService.consultorio.repository.dto.ConsultorioDTO;

@Repository
public class ConsultorioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final ConsultorioDTO dto = new ConsultorioDTO();

    @Autowired
    public ConsultorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono FROM medic.\"consultorio\" ";
        sql += request.getValuesOfConditions();
        List<ConsultorioDTO> consultorios = jdbcTemplate.query(sql, dto.consultorioRowMapper);
        return consultorios;
    }

    public ConsultorioDTO getConsultorio(Integer id) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono FROM medic.\"consultorio\" WHERE idconsultorio = ? ";
        
        ConsultorioDTO consultorio = jdbcTemplate.queryForObject(sql, dto.consultorioRowMapper, id);
        return consultorio;
    }

    public Integer setConsultorio(ConsultorioRequest consultorio) {
        String sql = "INSERT INTO medic.\"consultorio\" (ruc, nombre, ubicacion, direccion, telefono) VALUES (?, ?, ?, ?, ?) RETURNING idconsultorio";
        return jdbcTemplate.queryForObject(sql, Integer.class, consultorio.getRuc(), consultorio.getNombre(), consultorio.getUbicacion(), consultorio.getDireccion(), consultorio.getTelefono());
    }

    public boolean updateConsultorio(ConsultorioRequest consultorio) {
        String sql = "UPDATE medic.\"consultorio\" SET ruc = ?, nombre = ?, ubicacion = ?, direccion = ?, telefono = ? WHERE idconsultorio = ?";
        int rowsAffected = jdbcTemplate.update(sql, consultorio.getRuc(), consultorio.getNombre(), consultorio.getUbicacion(), consultorio.getDireccion(), consultorio.getTelefono(), consultorio.getIdconsultorio());
        return rowsAffected > 0;
    }

    public boolean deleteConsultorio(Integer id) {
        String sql = "DELETE FROM medic.\"consultorio\" WHERE idconsultorio = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
