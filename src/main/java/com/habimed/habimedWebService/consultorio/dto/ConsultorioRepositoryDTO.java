package com.habimed.habimedWebService.consultorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultorioRepositoryDTO {

    private String ruc;

    private String nombre;

    private String ubicacion;

    private String direccion;

    private String telefono;

    private Integer pagina;
    private Integer tamanioPagina;

    public String getConditions(){
        StringBuilder conditions = new StringBuilder("WHERE 1=1");

        if (ruc != null && !ruc.isEmpty()) {
            conditions.append(" AND ruc LIKE '%").append(ruc).append("%'");
        }
        if (nombre != null && !nombre.isEmpty()) {
            conditions.append(" AND nombre LIKE '%").append(nombre).append("%'");
        }
        if(ubicacion != null && !ubicacion.isEmpty()){
            conditions.append(" AND ubicacion LIKE '%").append(ubicacion).append("%'");
        }
        if(direccion != null && !direccion.isEmpty()){
            conditions.append(" AND direccion LIKE '%").append(direccion).append("%'");
        }
        if(telefono != null && !telefono.isEmpty()){
            conditions.append(" AND telefono LIKE '%").append(telefono).append("%'");
        }
        return conditions.toString();
    }
}
