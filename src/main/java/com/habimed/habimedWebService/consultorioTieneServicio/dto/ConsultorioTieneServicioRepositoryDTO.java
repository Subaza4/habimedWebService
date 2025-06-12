package com.habimed.habimedWebService.consultorioTieneServicio.dto;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicioRepositoryDTO {
    private Integer idconsultorio;
    private Integer idservicio;
    private Integer pagina;
    private Integer tamanioPagina;

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
