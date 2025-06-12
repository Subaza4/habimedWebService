package com.habimed.habimedWebService.cita.dto;

import java.util.Date;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    public RowMapper<CitaDTO> getCitaDTORowMapper() {
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

    public RowMapper<Cita> getCitaRowMapper() {
        return (rs, rowNum) -> {
            Cita cita = new Cita();
            cita.setIdcita(rs.getInt("idcita"));
            cita.setIddoctor(rs.getInt("iddoctor"));
            cita.setMotivo(rs.getString("motivo"));
            cita.setDescripcion(rs.getString("descripcion"));
            cita.setIdpaciente(rs.getInt("idpaciente"));
            
            // Convertir Timestamp a LocalDateTime
            Timestamp fechaInicio = rs.getTimestamp("fecha_hora_inicio");
            if (fechaInicio != null) {
                cita.setFecha_hora_inicio(fechaInicio.toLocalDateTime());
            }
            
            Timestamp fechaFin = rs.getTimestamp("fecha_hora_fin");
            if (fechaFin != null) {
                cita.setFecha_hora_fin(fechaFin.toLocalDateTime());
            }
            
            cita.setEstado(rs.getString("estado"));
            
            return cita;
        };
    }
}