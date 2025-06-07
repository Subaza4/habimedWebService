package com.habimed.habimedWebService.consultorio.dto;


import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioDTO {
    private Integer idconsultorio;

    private String ruc;

    private String nombre;

    private String ubicacion;

    private String direccion;

    private String telefono;

    // RowMapper para mapear un ResultSet a un objeto Consultorio
    public RowMapper<ConsultorioDTO> consultorioRowMapper = (rs, rowNum) -> {
        ConsultorioDTO consultorio = new ConsultorioDTO();
        consultorio.setIdconsultorio(rs.getInt("idconsultorio"));
        consultorio.setRuc(rs.getString("ruc"));
        consultorio.setNombre(rs.getString("nombre"));
        consultorio.setUbicacion(rs.getString("ubicacion"));
        consultorio.setDireccion(rs.getString("direccion"));
        consultorio.setTelefono(rs.getString("telefono"));
        return consultorio;
    };
}
