package com.habimed.habimedWebService.recomendacion.repository;

import com.habimed.habimedWebService.recomendacion.dto.RecomendacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecomendacionRepository {

    private JdbcTemplate jdbcTemplate;
    private RecomendacionDTO dto = new RecomendacionDTO();

    @Autowired
    public RecomendacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
