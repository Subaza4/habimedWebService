package com.habimed.habimedWebService.persona.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaResponse {
    private int dni;
    private String nombre;
    private String apellidos;
    private String correo;
    private String celular;
    private String direccion;
    private Date fecha_nacimiento;

    public PersonaResponse(ResultSet rs) {
        try {
            this.dni = rs.getInt("dni");
            this.nombre = rs.getString("nombre");
            this.apellidos = rs.getString("apellidos");
            this.correo = rs.getString("correo");
            this.celular = rs.getString("celular");
            this.direccion = rs.getString("direccion");
            this.fecha_nacimiento = rs.getDate("fecha_nacimiento");
        } catch (SQLException ex) {
            throw new RuntimeException("Error al construir PersonaResponse desde ResultSet", ex);
        }
    }
}
