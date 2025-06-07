package com.habimed.habimedWebService.tipoUsuario.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TipoUsuario {
    public TipoUsuario(int id, String nombre, String descripcion) {
        setId(id);
        setNombre(nombre);
        setDescripcion(descripcion);
    }
    //id, nombre, descripcion
    private int id;
    private String nombre;
    private String descripcion;
}
