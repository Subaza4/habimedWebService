package com.habimed.habimedWebService.diagnostico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiagnosticoRepositoryDTO {
    private Integer idcita;
    private String descripcion;
    private LocalDate fecha_diagnostico;
    private Integer pagina;
    private Integer tamanioPagina;

    /**
     * Genera condiciones SQL dinámicas basadas en los valores del objeto
     * @return Cadena con las condiciones WHERE
     */
    public String getConditions() {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idcita != null) {
            conditions.append(" AND \"idcita\" = ").append(idcita);
        }
        if (descripcion != null && !descripcion.isEmpty()) {
            conditions.append(" AND \"descripcion\" LIKE '%").append(descripcion).append("%'");
        }
        if (fecha_diagnostico != null) {
            conditions.append(" AND \"fecha_diagnostico\" = '").append(fecha_diagnostico).append("'");
        }
        return conditions.toString();
    }
}
