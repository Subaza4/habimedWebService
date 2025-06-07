package com.habimed.habimedWebService.persona.dto;

import org.springframework.jdbc.core.RowMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
            persona.setFecha_nacimiento(formatearFecha(rs.getDate("fecha_nacimiento")));

            return persona;
        };
    }

    private java.sql.Date formatearFecha(java.sql.Date fecha) {
        if (fecha == null) return null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String fechaFormateada = dateFormat.format(fecha);
            return java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd")
                    .format(dateFormat.parse(fechaFormateada)));
        } catch (ParseException e) {
            e.printStackTrace();
            return fecha;
        }
    }
}
