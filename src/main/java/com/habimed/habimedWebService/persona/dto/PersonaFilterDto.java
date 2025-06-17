package com.habimed.habimedWebService.persona.dto;

import com.habimed.parameterREST.RequestREST;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PersonaFilterDto extends RequestREST {
    private Long dni;
    private String nombres;
    private String apellidos;
    private String correo;
    private LocalDate fechaNacimiento;

    public void buildWhereClause(StringBuilder sql, List<Object> params) {
        if (this.nombres != null && !this.nombres.isEmpty()) {
            sql.append(" AND nombre LIKE ?");
            params.add("%" + this.nombres + "%");
        }
        if(this.apellidos != null && !this.apellidos.isEmpty()){
            sql.append(" AND apellidos LIKE ?");
            params.add("%" + this.apellidos + "%");
        }
        if (this.dni != null && this.dni > 0) {
            sql.append(" AND dni = ?");
            params.add("%" + this.dni + "%");
        }

        if (this.correo != null && !this.correo.isEmpty()) {
            sql.append(" AND email LIKE ?");
            params.add("%" + this.correo + "%");
        }
    }

}