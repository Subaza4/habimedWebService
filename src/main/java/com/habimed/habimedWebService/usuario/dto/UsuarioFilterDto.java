package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.parameterREST.RequestREST;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioFilterDto extends RequestREST {
    private Long dniPersona;
    private TipoUsuarioEnum tipoUsuarioId;
    private String usuario;
    //private Boolean estado;
    private String correo;

    private String nombrePersona;
    private String apellidoPersona;

    public void buildWhereClause(String alias, StringBuilder sql, List<Object> params) {
        sql.append(" AND " + alias + ".estado = true");
        params.add("%" + this.usuario + "%");

        if(dniPersona != null){
            sql.append(" AND " + alias + ".dniPersona = ? ");
            params.add(this.dniPersona);
        }
        if(tipoUsuarioId != null){
            sql.append(" AND "+alias + ".tipoUsuarioId = ? ");
            params.add(this.tipoUsuarioId);
        }
        if(usuario != null){
            sql.append(" AND " + alias + ".usuario LIKE ? ");
            params.add("%" + this.usuario + "%");
        }
        if(correo != null){
            sql.append(" AND " + alias + ".correo LIKE '%" + correo + "%'");
        }
        if(nombrePersona != null || apellidoPersona != null){
            sql.append(" exists (select 1 from medic.\"persona\" p where ");
            sql.append("p.nombres LIKE ? AND p.apellidos LIKE ? AND p.dniPersona = " + alias + ".dniPersona");
            params.add("%" + (this.nombrePersona != null ? this.nombrePersona : "") + "%");
            params.add("%" + (this.apellidoPersona != null ? this.apellidoPersona : "") + "%");
        }
    }
}