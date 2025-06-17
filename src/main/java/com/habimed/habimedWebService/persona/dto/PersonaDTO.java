package com.habimed.habimedWebService.persona.dto;

import org.springframework.jdbc.core.RowMapper;
import java.time.LocalDate;
import com.habimed.habimedWebService.persona.domain.model.Persona;

public class PersonaDTO {
    // Row mapper
    public RowMapper<Persona> productRowMapper() {
        return (rs, rowNum) -> {
            Persona persona = new Persona();
            persona.setDni(rs.getLong("dni"));
            persona.setNombres(rs.getString("nombres"));
            persona.setApellidos(rs.getString("apellidos"));
            //persona.setCorreo(rs.getString("correo"));
            persona.setCelular(rs.getString("celular"));
            persona.setDireccion(rs.getString("direccion"));
            persona.setFecha_nacimiento(formatearFecha(rs.getDate("fecha_nacimiento")));

            return persona;
        };
    }

    private LocalDate formatearFecha(java.sql.Date fecha) {
        if (fecha == null) return null;
        // Conversión directa de java.sql.Date a LocalDate
        return fecha.toLocalDate();
    }
}