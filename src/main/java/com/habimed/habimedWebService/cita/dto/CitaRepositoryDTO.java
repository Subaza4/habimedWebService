package com.habimed.habimedWebService.cita.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaRepositoryDTO {
    private Integer iddoctor;

    private String motivo;

    private String descripcion;

    private Integer idpaciente;

    private Timestamp fecha_hora_inicio;

    private Timestamp fecha_hora_fin;

    private Integer estado; // Asignar valor por medio de un ENUM

    private Integer pagina;
    private Integer tamanioPagina;

    /**
     * Genera la cláusula WHERE para consultas SQL basada en los atributos no nulos
     * @return String con la cláusula WHERE para filtrar los resultados
     */
    public String getConditions() {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");

        if (idpaciente != null) {
            conditions.append(" AND \"idpaciente\" = ").append(idpaciente);
        }
        if (iddoctor != null) {
            conditions.append(" AND \"iddoctor\" = ").append(iddoctor);
        }
        if (motivo != null && !motivo.isEmpty()) {
            conditions.append(" AND \"motivo\" LIKE '%").append(motivo).append("%'");
        }
        /*if (fecha_hora_inicio != null) {
            conditions.append(" AND \"fecha_hora_inicio\" = '").append(fecha_hora_inicio).append("'");
        }
        if (fecha_hora_fin != null) {
            conditions.append(" AND \"fecha_hora_fin\" = '").append(fecha_hora_fin).append("'");
        }*/
        if (estado != null) {
            conditions.append(" AND \"estado\" = '").append(estado).append("'");
        }
        if (descripcion != null && !descripcion.isEmpty()) {
            conditions.append(" AND \"descripcion\" LIKE '%").append(descripcion).append("%'");
        }
        if (idpaciente != null) {
            conditions.append(" AND \"idpaciente\" = ").append(idpaciente);
        }

        // Condiciones para búsqueda por rango de fechas
        if (fecha_hora_inicio != null) {
            conditions.append(" AND \"fecha_hora_inicio\" >= '").append(fecha_hora_inicio).append("'");
        }
        if (fecha_hora_fin != null) {
            conditions.append(" AND \"fecha_hora_inicio\" <= '").append(fecha_hora_fin).append("'");
        }

        return conditions.toString();
    }
}