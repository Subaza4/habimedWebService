package com.habimed.habimedWebService.cita.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequest extends RequestREST {
    private Integer idcita;
    private Integer idusuario;
    private Integer iddoctorconsultorio;
    private Timestamp fechainicio;
    private Timestamp fechafin;
    private String motivo;
    private String descripcion;
    private EstadoCitaEnum estado;

    public String getValuesOfConditions() {
        StringBuilder conditions = new StringBuilder(" WHERE 1=1 ");
        
        if (idusuario != null) {
            conditions.append(" AND u.\"dnipersona\" = ").append(idusuario);
        }
        if (iddoctorconsultorio != null) {
            conditions.append(" AND dtc.\"iddoctor\" = ").append(iddoctorconsultorio);
        }
        if (fechainicio != null) {
            conditions.append(" AND c.\"fecha_hora_inicio\" >= '").append(fechainicio).append("'");
        }
        if (fechafin != null) {
            conditions.append(" AND c.\"fecha_hora_inicio\" <= '").append(fechafin).append("'");
        }
        if (estado != null) {
            conditions.append(" AND c.\"estado\" = '").append(estado.ordinal()).append("'");
        }

        return conditions.toString();
    }
}
