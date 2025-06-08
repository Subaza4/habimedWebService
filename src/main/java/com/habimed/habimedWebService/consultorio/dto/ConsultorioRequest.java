package com.habimed.habimedWebService.consultorio.dto;

import com.habimed.parameterREST.RequestREST;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultorioRequest extends RequestREST {

    // idconsultorio es INT GENERATED ALWAYS AS IDENTITY,
    // por lo que no debería ser enviado en el request para CREATE, pero sí para UPDATE.
    // Lo hacemos opcional (Integer) para que sea nulo al crear.
    private Integer idconsultorio;

    @Size(max = 11, message = "El RUC debe tener máximo 11 caracteres.")
    private String ruc;

    @Size(max = 45, message = "El nombre debe tener máximo 45 caracteres.")
    private String nombre;

    @Size(max = 45, message = "La ubicación debe tener máximo 45 caracteres.")
    private String ubicacion;

    @Size(max = 45, message = "La dirección debe tener máximo 45 caracteres.")
    private String direccion;

    @Size(max = 8, message = "El teléfono debe tener máximo 8 caracteres.")
    private String telefono;

    public String getConditions(){
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idconsultorio != null || idconsultorio > 0) {
            conditions.append(" AND idconsultorio = ").append(idconsultorio);
        }
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