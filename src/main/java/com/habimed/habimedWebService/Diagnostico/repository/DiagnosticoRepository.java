package com.habimed.habimedWebService.diagnostico.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;

@Repository
public class DiagnosticoRepository {
    
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DiagnosticoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DiagnosticoDTO> getAllDiagnosticos(DiagnosticoRequest request) {
        // Implementación para obtener todos los diagnósticos
        return jdbcTemplate.query("SQL_QUERY_HERE", new DiagnosticoDTO().getRowMapper());
    }

    public DiagnosticoDTO getDiagnosticoById(Integer iddiagnostico) {
        // Implementación para obtener un diagnóstico por ID
        return jdbcTemplate.queryForObject("SQL_QUERY_HERE", new Object[]{iddiagnostico}, new DiagnosticoDTO().getRowMapper());
    }

    public Integer setDiagnostico(DiagnosticoRequest request) {
        // Implementación para insertar o actualizar un diagnóstico
        String sql = "INSERT INTO diagnostico (idcita, descripciondiagnostico, fechadiagnostico) VALUES (?, ?, ?) RETURNING iddiagnostico";
        return jdbcTemplate.queryForObject(sql, new Object[]{
            request.getIdcita(),
            request.getDescripcion(),
            request.getFecha_diagnostico()
        }, Integer.class);
    }

    public boolean deleteDiagnostico(Integer iddiagnostico) {
        // Implementación para eliminar un diagnóstico
        String sql = "DELETE FROM diagnostico WHERE iddiagnostico = ?";
        int rowsAffected = jdbcTemplate.update(sql, iddiagnostico);
        return rowsAffected > 0;
    }
}
