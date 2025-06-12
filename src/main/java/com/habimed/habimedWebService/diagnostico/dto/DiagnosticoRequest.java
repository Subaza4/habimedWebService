package com.habimed.habimedWebService.diagnostico.dto;

import java.time.LocalDate;
import java.util.Map;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticoRequest {
    private Integer idcita;
    private String descripcion;
    private LocalDate fecha_diagnostico;

    public Map<String, String> getValuesOfConditions() {
        Map<String, String> conditions = new java.util.HashMap<>();
        if (idcita != null && idcita > 0)
            conditions.put("idcita", String.valueOf(idcita));
        if (descripcion != null && !descripcion.isEmpty())
            conditions.put("descripcion", descripcion);
        if (fecha_diagnostico != null)
            conditions.put("fecha_diagnostico", fecha_diagnostico.toString());

        return conditions;
    }
}
