package com.habimed.habimedWebService.consultorioTieneServicio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicioRequest {
    private Integer idconsultorio;
    private Integer idservicio;

    public String getValuesOfConditions() {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idconsultorio != null) {
            conditions.append(" AND idconsultorio = ").append(idconsultorio);
        }
        if (idservicio != null) {
            conditions.append(" AND idservicio = ").append(idservicio);
        }
        return conditions.toString();
    }
}
