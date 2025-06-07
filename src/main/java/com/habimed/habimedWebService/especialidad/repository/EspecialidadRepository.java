package com.habimed.habimedWebService.especialidad.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadDTO;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadRequest;

@Repository
public class EspecialidadRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final EspecialidadDTO dto = new EspecialidadDTO();

    @Autowired
    public EspecialidadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Especialidad> getEspecialidades(){
        String sql = "SELECT * FROM medic.\"especialidad\"";
        return jdbcTemplate.query(sql, dto.especialidadRowMapper());
    }

    public Especialidad getEspecialidad(Integer idEspecialidad) {
        String sql = "SELECT * FROM medic.\"especialidad\" WHERE \"idespecialidad\" = ?";
        return jdbcTemplate.queryForObject(sql, dto.especialidadRowMapper(), idEspecialidad);
    }

    public Integer setEspecialidad(EspecialidadRequest especialidad) {
        //CAMBIAR POR UN LLAMADO A UN PROCEDURE ALMACENADO
        // String sql = "CALL medic.\"setEspecialidad\"(?, ?) RETURNING \"idespecialidad\"";
        String sql = "INSERT INTO medic.\"especialidad\" (\"nombre\", \"descripcion\") VALUES (?, ?) RETURNING \"idespecialidad\"";
        return jdbcTemplate.queryForObject(sql, Integer.class, especialidad.getNombre(), especialidad.getDescripcion());
    }

    public boolean deleteEspecialidad(Integer idEspecialidad) {
        String sql = "DELETE FROM medic.\"especialidad\" WHERE \"idespecialidad\" = ?";
        int rowsAffected = jdbcTemplate.update(sql, idEspecialidad);
        return rowsAffected > 0;
    }
}
