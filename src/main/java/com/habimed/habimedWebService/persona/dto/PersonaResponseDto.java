package com.habimed.habimedWebService.persona.dto;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Data
public class PersonaResponseDto {
    private Long dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private String celular;
    private String direccion;
    private LocalDate fechaNacimiento;

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
