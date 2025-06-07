package com.habimed.habimedWebService.resenia.repository;

import com.habimed.habimedWebService.resenia.dto.ReseniaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReseniaRepository {
    private JdbcTemplate jdbcTemplate;
    private ReseniaDTO dto = new ReseniaDTO();

    public ReseniaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
