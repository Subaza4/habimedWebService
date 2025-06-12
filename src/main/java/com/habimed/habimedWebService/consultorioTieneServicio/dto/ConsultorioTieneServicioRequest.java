package com.habimed.habimedWebService.consultorioTieneServicio.dto;

import com.habimed.parameterREST.RequestREST;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicioRequest {
    @NotNull(message = "Se requiere del id del Consultorio para eliminar la relación")
    private Integer idConsultorio;
    @NotNull(message = "Se requiere del id del Serviccio para eliminar la relación")
    private Integer idServicio;

    public String getConditions(String alias) {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idConsultorio != null) {
            conditions.append(" AND ").append(alias).append(".\"idconsultorio\" = ").append(idConsultorio);
        }
        if (idServicio != null) {
            conditions.append(" AND ").append(alias).append(".\"idservicio\" = ").append(idServicio);
        }
        return conditions.toString();
    }
}
