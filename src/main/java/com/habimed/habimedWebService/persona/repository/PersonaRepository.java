package com.habimed.habimedWebService.persona.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Persona> findAll() {
        String sql = "SELECT * FROM medic.\"persona\"";
        return jdbcTemplate.query(sql, dto.productRowMapper());
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
    public int save(PersonaRequest persona) {

        String sql = "INSERT INTO medic.\"persona\" (dni, nombre, apellidos, correo, celular, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, persona.getDni());
            ps.setString(2, persona.getNombre());
            ps.setString(3, persona.getApellidos());
            ps.setString(4, persona.getCorreo());
            ps.setString(5, persona.getCelular());
            ps.setDate(6, persona.getFecha_nacimiento());
            return ps;
        });
        return rowsAffected;
    }

    // Update persona
    public boolean update(Persona persona) {
        /* String sql = "UPDATE medic.\"productos\" SET \"nombre\" = ?, \"descripcion\" = ?, \"precio\" = ?, \"stock\" = ? WHERE \"id\" = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                persona.getNombre(),
                persona.getDescripcion(),
                persona.getPrecio(),
                persona.getStock(),
                persona.getId());
        return rowsAffected > 0; */
        return true;
    }

    // Delete persona
    public boolean deleteById(Long id) {
        /* String sql = "DELETE FROM medic.\"persona\" WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0; */
        return true;
    }
}
