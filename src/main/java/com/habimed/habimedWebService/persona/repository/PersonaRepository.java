package com.habimed.habimedWebService.persona.repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.habimed.habimedWebService.persona.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class PersonaRepository {

    @Value("${habimed.default.pagina}")
    private static Integer NumElementos;
    @Value("${habimed.default.numelementos}")
    private static Integer NumPagina;

    private final JdbcTemplate jdbcTemplate;
    private final PersonaResponseDto dto = new PersonaResponseDto();

    @Autowired
    public PersonaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find all personas
    public List<Persona> findAll(PersonaFilterDto request) {
        StringBuilder sql = new StringBuilder("SELECT * FROM medic.\"persona\" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        // Construcción segura de condiciones
        request.buildWhereClause(sql, params);

        sql.append(" ORDER BY dni DESC LIMIT ? OFFSET ?");

        Integer limit = request.getNum_elementos() > 0 ? request.getNum_elementos() : NumElementos;
        Integer offset = request.getPagina() > 0 ? (request.getPagina() -1) : NumPagina;

        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), params.toArray(), personaRowMapper());
    }


    public Optional<Persona> findById(Long dni) {
        String sql = "SELECT * FROM medic.\"persona\" WHERE dni = ?";

        try {
            Persona persona = jdbcTemplate.queryForObject(sql, personaRowMapper(), dni);
            return Optional.of(persona);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Save new persona
    public Integer setPersona(Persona persona) {
        String sql = "CALL medic.\"upsert_persona\"(?, ?, ?, ?, ?, ?, ?)";
        int[] resultado = new int[1];
        
        try {
            // Lambda
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar persona
                    if (persona == null) {
                        throw new IllegalArgumentException("La persona no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setLong(1, persona.getDni());
                    cs.setString(2, persona.getNombres());
                    cs.setString(3, persona.getApellidos());
                    cs.setString(4, persona.getCelular());
                    cs.setString(5, persona.getDireccion());
                    cs.setDate(6, java.sql.Date.valueOf
                            (persona.getFecha_nacimiento()));
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(7, Types.INTEGER);
                    cs.execute();

                    // Obtener el resultado
                    resultado[0] = cs.getInt(7);
                    return null;
                }
            });
            
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la persona", e);
        }
    }

    // Delete persona
    public Boolean deleteById(Long id) {
        try{
            String sql = "DELETE FROM medic.\"persona\" WHERE dni = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////
    private RowMapper<Persona> personaRowMapper() {
        return (rs, rowNum) -> {
            Persona persona = new Persona();
            persona.setDni(rs.getLong("dni"));
            persona.setNombres(rs.getString("nombres"));
            persona.setApellidos(rs.getString("apellidos"));
            persona.setCelular(rs.getString("celular"));
            persona.setDireccion(rs.getString("direccion"));
            persona.setFecha_nacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
            return persona;
        };
    }
}