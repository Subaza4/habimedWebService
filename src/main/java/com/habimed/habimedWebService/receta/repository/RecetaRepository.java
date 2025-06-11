package com.habimed.habimedWebService.receta.repository;

import com.habimed.habimedWebService.receta.dto.RecetaDTO;
import com.habimed.habimedWebService.receta.dto.RecetaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecetaRepository {

    private JdbcTemplate jdbcTemplate;
    private RecetaDTO dto = new RecetaDTO();

    @Autowired
    public RecetaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RecetaDTO> getRecetas(RecetaRequest request) {
        return null;
    }

    public void setReceta(String sql) {

    }

    public void deleteReceta(String sql) {

    }

}
