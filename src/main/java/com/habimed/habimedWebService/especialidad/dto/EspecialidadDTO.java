package com.habimed.habimedWebService.especialidad.dto;

import org.springframework.jdbc.core.RowMapper;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadDTO {

    private Integer idespecialidad;

    private String nombre;

    private String descripcion;


    public RowMapper<Especialidad> especialidadRowMapper() {
        return (rs, rowNum) -> {
            Especialidad especialidad = new Especialidad();
            especialidad.setIdEspecialidad(rs.getInt("idespecialidad"));
            especialidad.setNombre(rs.getString("nombre"));
            especialidad.setDescripcion(rs.getString("descripcion"));
            
            return especialidad;
        };
    }
}