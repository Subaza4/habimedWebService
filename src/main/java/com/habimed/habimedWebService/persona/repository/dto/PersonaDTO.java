package com.habimed.habimedWebService.persona.repository.dto;

import org.springframework.jdbc.core.RowMapper;

import com.habimed.habimedWebService.persona.domain.model.Persona;

public class PersonaDTO {
    // Row mapper
    public RowMapper<Persona> productRowMapper() {
        return (rs, rowNum) -> {
            Persona persona = new Persona();
            persona.setDni(rs.getLong("dni"));
            persona.setNombre(rs.getString("nombres"));
            persona.setApellidos(rs.getString("apellidos"));
            persona.setCorreo(rs.getString("correo"));
            persona.setCelular(rs.getString("celular"));
            persona.setDireccion(rs.getString("direccion"));
            persona.setFecha_nacimiento(rs.getDate("fecha_nacimiento"));
            return persona;
        };
    }
}
