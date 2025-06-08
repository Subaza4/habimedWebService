package com.habimed.habimedWebService.especialidad.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadDTO;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EspecialidadRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final EspecialidadDTO dto = new EspecialidadDTO();

    @Autowired
    public EspecialidadRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Especialidad> getEspecialidades(EspecialidadRequest request){
        String sql = "SELECT * FROM medic.\"especialidad\" " + request.getValuesOfConditions() + "OFFSET ? LIMIT ?";
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() - 1) * size;

        return jdbcTemplate.query(sql, dto.especialidadRowMapper(), pagina, size);
    }

    public Especialidad getEspecialidad(Integer idEspecialidad) {
        String sql = "SELECT * FROM medic.\"especialidad\" WHERE \"idespecialidad\" = ?";
        return jdbcTemplate.queryForObject(sql, dto.especialidadRowMapper(), idEspecialidad);
    }

    public Integer setEspecialidad(EspecialidadRequest especialidad) {
        String sql = "CALL medic.upsert_especialidad(?, ?, ?, ?)";

        // Crear un Map para almacenar el parámetro de salida
        Map<String, Object> inOutParameters = new HashMap<>();
        inOutParameters.put("p_resultado", Integer.class);

        // Ejecutar el procedimiento almacenado con SimpleJdbcCall
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withSchemaName("medic")
            .withProcedureName("upsert_especialidad")
            .declareParameters(
                new SqlParameter("p_idespecialidad", Types.INTEGER),
                new SqlParameter("p_nombre", Types.VARCHAR),
                new SqlParameter("p_descripcion", Types.VARCHAR),
                new SqlOutParameter("p_resultado", Types.INTEGER)
            );

        // Preparar los parámetros
        SqlParameterSource parameterSource = new MapSqlParameterSource()
            .addValue("p_idespecialidad", especialidad.getIdespecialidad())
            .addValue("p_nombre", especialidad.getNombre())
            .addValue("p_descripcion", especialidad.getDescripcion())
            .addValue("p_resultado", 0); // Valor inicial del parámetro de salida

        // Ejecutar el procedimiento y obtener el resultado
        Map<String, Object> result = jdbcCall.execute(parameterSource);

        // Retornar el valor del parámetro de salida
        return (Integer) result.get("p_resultado");
    }

    public boolean deleteEspecialidad(Integer idEspecialidad) {
        String sql = "DELETE FROM medic.\"especialidad\" WHERE \"idespecialidad\" = ?";
        int rowsAffected = jdbcTemplate.update(sql, idEspecialidad);
        return rowsAffected > 0;
    }
}