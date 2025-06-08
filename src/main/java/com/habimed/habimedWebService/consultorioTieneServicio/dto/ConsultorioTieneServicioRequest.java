package com.habimed.habimedWebService.consultorioTieneServicio.dto;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicioRequest extends RequestREST {
    private Integer idconsultorio;
    private Integer idservicio;

    public String getConditions(String alias) {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idconsultorio != null) {
            conditions.append(" AND ").append(alias).append(".\"idconsultorio\" = ").append(idconsultorio);
        }
        if (idservicio != null) {
            conditions.append(" AND ").append(alias).append(".\"idservicio\" = ").append(idservicio);
        }
        return conditions.toString();
    }
}
