package com.habimed.habimedWebService.servicio.repository.dto;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.habimed.habimedWebService.servicio.domain.parameter.response.ServicioResponse;

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

    public RowMapper<ServicioResponse> getServicioRowMapper() {
        return (rs, rowNum) -> {
            ServicioResponse servicio = new ServicioResponse();
            servicio.setIdservicio(rs.getInt("idservicio"));
            servicio.setNombre(rs.getString("nombre"));
            servicio.setDescripcion(rs.getString("descripcion"));
            servicio.setRiesgos(rs.getString("riesgos"));
            servicio.setIdespecialidad(rs.getInt("idespecialidad"));
            return servicio;
        };
    }
//repositorio
    public String buildCondition(Map<String, String> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" WHERE ");
        boolean first = true;
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            if (!first) {
                sb.append(" AND ");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            if ("idservicio".equalsIgnoreCase(key) || "idespecialidad".equalsIgnoreCase(key)) {
                sb.append(key).append(" = ").append(value);
            } else {
                sb.append(key).append(" LIKE '%").append(value).append("%'");
            }
            first = false;
        }
        return sb.toString();
    }
}
