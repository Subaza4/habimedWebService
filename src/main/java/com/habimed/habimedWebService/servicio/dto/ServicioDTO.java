package com.habimed.habimedWebService.servicio.dto;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Integer idservicio; // idservicio INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    private Integer idespecialidad; // idespecialidad INT NOT NULL (Clave foránea)

    private String nombre; // nombre VARCHAR(100) NOT NULL

    private String descripcion; // descripcion VARCHAR(500) NULL

    private String riesgos; // riesgos VARCHAR(500) NULL

    private Especialidad especialidad;

    public RowMapper<ServicioDTO> getServicioRowMapper() {
        return (rs, rowNum) -> {
            ServicioDTO servicio = new ServicioDTO();
            servicio.setIdservicio(rs.getInt("idservicio"));
            servicio.setNombre(rs.getString("nombre"));
            servicio.setDescripcion(rs.getString("descripcion"));
            servicio.setRiesgos(rs.getString("riesgos"));
            servicio.setIdespecialidad(rs.getInt("idespecialidad"));
            Especialidad especialidad1 = new Especialidad();
            especialidad1.setIdEspecialidad(rs.getInt("idespecialidad"));
            especialidad1.setNombre(rs.getString("especialidad_nombre"));
            especialidad1.setDescripcion(rs.getString("especialidad_descripcion"));
            return servicio;
        };
    }
}
