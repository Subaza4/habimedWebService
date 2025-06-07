package com.habimed.habimedWebService.cita.dto;

import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {
    private Integer idcita;
    private Date fechacita;
    private String estado;
    private String estadopago;
    private Integer idusuario;
    private String nombreUsuario;
    private Integer iddetallepago;
    private Float montopago;
    private String nombreconsultorio;
    private String direccionconsultorio;
    private String nombredoctor;

    public RowMapper<CitaDTO> getCitaRowMapper() {
        return (rs, rowNum) -> new CitaDTO(
            rs.getInt("idcita"),
            rs.getDate("fechacita"),
            rs.getString("estado"),
            rs.getString("estadopago"),
            rs.getInt("idusuario"),
            rs.getString("nombreusuario"),
            rs.getInt("iddetallepago"),
            rs.getFloat("montopago"),
            rs.getString("nombreconsultorio"),
            rs.getString("direccionconsultorio"),
            rs.getString("nombredoctor")
        );
    }

    
}
