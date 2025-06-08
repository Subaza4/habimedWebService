package com.habimed.habimedWebService.persona.repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.PersonaDTO;
import com.habimed.habimedWebService.persona.dto.PersonaRequest;

@Repository
public class PersonaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PersonaDTO dto = new PersonaDTO();
    
    @Autowired
    public PersonaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Find all personas
    public List<Persona> findAll(PersonaRequest request) {
        String sql = "SELECT * FROM medic.\"persona\" " + request.buildConditions() + " OFFSET ? LIMIT ?";
        //paginar resultados
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() - 1) * size;
        // Example query: SELECT * FROM medic."persona" WHERE nombre LIKE '%John%' OFFSET 0 LIMIT 10

        return jdbcTemplate.query(sql, dto.productRowMapper(), pagina, size);
    }

    // Find persona by ID
    public Optional<Persona> findById(Long dni) {
        String sql = "SELECT * FROM medic.\"persona\" WHERE dni = ?";
        List<Persona> personas = jdbcTemplate.query(sql, dto.productRowMapper(), dni);
        if (personas.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(personas.get(0));
        }
    }

    // Save new persona
    public int setPersona(PersonaRequest persona) {
        String sql = "CALL medic.\"upsert_persona\"(?, ?, ?, ?, ?, ?, ?, ?)";
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
                    cs.setString(2, persona.getNombre());
                    cs.setString(3, persona.getApellidos());
                    cs.setString(4, persona.getCorreo());
                    cs.setString(5, persona.getCelular());
                    cs.setString(6, persona.getDireccion());
                    cs.setDate(7, persona.getFecha_nacimiento());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(8, Types.INTEGER);
                    cs.execute();
                    
                    // Obtener el resultado
                    resultado[0] = cs.getInt(8);
                    return null;
                }
            });
            
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la persona", e);
        }
    }

    // Delete persona
    public boolean deleteById(Long id) {
        try{
            String sql = "DELETE FROM medic.\"persona\" WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}