package com.habimed.habimedWebService.consultorio.domain.parameter.consultorioRequest;

import java.util.Map;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultorioRequest {

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


    public Map<String, String> getValuesOfConditions() {
        Map<String, String> conditions = new java.util.HashMap<>();
        if (idconsultorio != null && idconsultorio > 0)
            conditions.put("idconsultorio", String.valueOf(idconsultorio));
        if (ruc != null && !ruc.isEmpty())
            conditions.put("ruc", ruc);
        if (nombre != null && !nombre.isEmpty())
            conditions.put("nombre", nombre);
        if (ubicacion != null && !ubicacion.isEmpty())
            conditions.put("ubicacion", ubicacion);
        if (direccion != null && !direccion.isEmpty())
            conditions.put("direccion", direccion);
        if (telefono != null && !telefono.isEmpty())
            conditions.put("telefono", telefono);

        return conditions;
    }

}