package com.habimed.habimedWebService.receta.dto;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetaRequest extends RequestREST {
    private Integer idreceta;
    private Integer idcita;
    private Integer descripcion;
    private String fecha_receta;

    public String getConditions(String alias) {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idreceta != null) {
            conditions.append(" AND ").append(alias).append(".\"idreceta\" = ").append(idreceta);
        }
        if (idcita != null) {
            conditions.append(" AND ").append(alias).append(".\"idcita\" = ").append(idcita);
        }
        if (descripcion != null) {
            conditions.append(" AND ").append(alias).append(".\"descripcion\" = ").append(descripcion);
        }
        if (fecha_receta != null) {
            conditions.append(" AND ").append(alias).append(".\"fecha_receta\" = '").append(fecha_receta).append("'");
        }
        return conditions.toString();
    }
}
