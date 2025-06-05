package com.habimed.habimedWebService.horarioDoctor.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorRequest;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorResponse;

@Repository
public class HorarioDoctorRepository {
    private final JdbcTemplate jdbcTemplate;
    private final HorarioDoctorResponse dto = new HorarioDoctorResponse();

    @Autowired
    public HorarioDoctorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<HorarioDoctorResponse> getAllHorarios(HorarioDoctorRequest request) {
        String sql = "SELECT * FROM medic.\"horariodoctor\"";
        return jdbcTemplate.query(sql, dto.horarioDoctorRowMapper());
    }

    public HorarioDoctorResponse getHorarioById(Integer idhorariodoctor) {
        String sql = "SELECT * FROM medic.\"horariodoctor\" WHERE idhorariodoctor = ?";
        return jdbcTemplate.queryForObject(sql, dto.horarioDoctorRowMapper(), idhorariodoctor);
    }

    public Integer setHorario(HorarioDoctorRequest request) {
        String sql = "INSERT INTO medic.\"horariodoctor\" (iddoctor, dia_semana, hora_inicio, hora_fin, duracion_minutos) "
                   + "VALUES (?, ?, ?, ?, ?) RETURNING idhorariodoctor";
        return jdbcTemplate.queryForObject(sql, Integer.class, 
            request.getIddoctor(), 
            request.getDiaSemana(), 
            request.getHoraInicio(), 
            request.getHoraFin(), 
            request.getDuracionMinutos());
    }

    public boolean deleteHorario(Integer idhorariodoctor) {
        String sql = "DELETE FROM medic.\"horariodoctor\" WHERE idhorariodoctor = ?";
        return jdbcTemplate.update(sql, idhorariodoctor) > 0;
    }
}
