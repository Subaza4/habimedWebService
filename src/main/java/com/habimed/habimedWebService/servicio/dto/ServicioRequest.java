package com.habimed.habimedWebService.servicio.dto;

import com.habimed.parameterREST.RequestREST;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioRequest extends RequestREST {
    private Integer idservicio; // idservicio INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    @NotNull(message = "El ID de especialidad no puede ser nulo.")
    private Integer idespecialidad; // idespecialidad INT NOT NULL (Clave foránea)

    @NotBlank(message = "El nombre del servicio no puede estar vacío.")
    @Size(max = 100, message = "El nombre del servicio debe tener máximo 100 caracteres.")
    private String nombre; // nombre VARCHAR(100) NOT NULL

    @Size(max = 500, message = "La descripción del servicio debe tener máximo 500 caracteres.")
    private String descripcion; // descripcion VARCHAR(500) NULL

    @Size(max = 500, message = "La descripción de riesgos debe tener máximo 500 caracteres.")
    private String riesgos; // riesgos VARCHAR(500) NULL


    public Map<String, String> getValuesOfConditions() {
        Map<String, String> conditions = new java.util.HashMap<>();
        if (idservicio != null && idservicio > 0)
            conditions.put("idservicio", String.valueOf(idservicio));
        if (idespecialidad != null && idespecialidad > 0)
            conditions.put("idespecialidad", String.valueOf(idespecialidad));
        if (nombre != null && !nombre.isEmpty())
            conditions.put("nombre", nombre);
        if (descripcion != null && !descripcion.isEmpty())
            conditions.put("descripcion", descripcion);
        if (riesgos != null && !riesgos.isEmpty())
            conditions.put("riesgos", riesgos);

        return conditions;
    }

    public String getConditions(){
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idservicio != null) {
            conditions.append(" AND idservicio = ").append(idservicio);
        }
        if (idespecialidad != null) {
            conditions.append(" AND idespecialidad = ").append(idespecialidad);
        }
        if (nombre != null && !nombre.isEmpty()) {
            conditions.append(" AND nombre LIKE '%").append(nombre).append("%'");
        }
        if (descripcion != null && !descripcion.isEmpty()) {
            conditions.append(" AND descripcion LIKE '%").append(descripcion).append("%'");
        }
        if (riesgos != null && !riesgos.isEmpty()) {
            conditions.append(" AND riesgos LIKE '%").append(riesgos).append("%'");
        }
        return conditions.toString();
    }
}