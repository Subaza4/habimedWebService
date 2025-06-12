package com.habimed.habimedWebService.persona.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.habimed.parameterREST.RequestREST;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRequest extends RequestREST {
    private Long dni;
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;
    @Size(min = 1, max = 100, message = "Los apellidos deben tener entre 1 y 100 caracteres")
    private String apellidos;
    @Size(max = 100, message = "El correo no debe exceder los 100 caracteres")
    private String correo;
    @Size(max = 15, message = "El celular no debe exceder los 15 caracteres")
    private String celular;
    @Size(max = 255, message = "La dirección no debe exceder los 255 caracteres")
    private String direccion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha_nacimiento;

    public String buildConditions() {
        Map<String, String> conditions = getValuesOfConditions();
        StringBuilder conditionsString = new StringBuilder();
        Boolean isFirst = true;
        for (String key : conditions.keySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                conditionsString.append(" AND ");
            }

            if (key.equalsIgnoreCase("nombre") || key.equalsIgnoreCase("apellidos") || key.equalsIgnoreCase("correo") || key.equalsIgnoreCase("celular") || key.equalsIgnoreCase("direccion")) {
                conditionsString.append(key).append(" LIKE '%").append(conditions.get(key)).append("%'");
            } else {
                conditionsString.append(key).append(" = '").append(conditions.get(key)).append("'");
            }
        }
        return !conditionsString.isEmpty() ? " WHERE " + conditionsString.toString() : "";
    }

    public Map<String, String> getValuesOfConditions() {
        Map<String, String> conditions = new java.util.HashMap<>();
        if (dni != null && dni > 0)
            conditions.put("dni", String.valueOf(dni));
        if (nombre != null && !nombre.isEmpty())
            conditions.put("nombre", nombre);
        if (apellidos != null && !apellidos.isEmpty())
            conditions.put("apellidos", apellidos);
        if (correo != null && !correo.isEmpty())
            conditions.put("correo", correo);
        if (celular != null && !celular.isEmpty())
            conditions.put("celular", celular);
        if (direccion != null && !direccion.isEmpty())
            conditions.put("direccion", direccion);
        if (fecha_nacimiento != null)
            conditions.put("fecha_nacimiento", fecha_nacimiento.toString());

        return conditions;
    }

    // columnas para mostrar en un select con sus etiquetas tabla_columna
    public String getColumnasSelect(String alias){
        StringBuilder columnas = new StringBuilder();
        columnas.append(alias).append(".dni as persona_dni, ");
        columnas.append(alias).append(".nombre as persona_nombre, ");
        columnas.append(alias).append(".apellidos as persona_apellidos, ");
        columnas.append(alias).append(".correo as persona_correo, ");
        columnas.append(alias).append(".celular as persona_celular, ");
        columnas.append(alias).append(".direccion as persona_direccion, ");
        columnas.append(alias).append(".fecha_nacimiento as persona_fecha_nacimiento ");
        return columnas.toString();
    }
}