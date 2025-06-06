package com.habimed.habimedWebService.diagnostico.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticoDTO {
    private Long iddiagnostico;
    private Long idcita;
    private String descripciondiagnostico;
    private LocalDate fechadiagnostico;

    // Campos de la cita relacionados (opcional, para enriquecer el DTO)
    private String motivocita;
    private LocalDateTime fechoracita;
    private String estadocita;
    private String nombrepaciente;
    private String apellidopaciente;
    private String nombredoctor;
    private String apellidodoctor;

    public RowMapper<DiagnosticoDTO> getRowMapper() {
        return (rs, rowNum) -> {
            DiagnosticoDTO diagnostico = new DiagnosticoDTO();
            diagnostico.setIddiagnostico(rs.getLong("iddiagnostico"));
            diagnostico.setIdcita(rs.getLong("idcita"));
            diagnostico.setDescripciondiagnostico(rs.getString("descripciondiagnostico"));
            diagnostico.setFechadiagnostico(rs.getObject("fechadiagnostico", LocalDate.class));
            diagnostico.setMotivocita(rs.getString("motivocita"));
            diagnostico.setFechoracita(rs.getObject("fechoracita", LocalDateTime.class));
            diagnostico.setEstadocita(rs.getString("estadocita"));
            diagnostico.setNombrepaciente(rs.getString("nombrepaciente"));
            diagnostico.setApellidopaciente(rs.getString("apellidopaciente"));
            diagnostico.setNombredoctor(rs.getString("nombredoctor"));
            diagnostico.setApellidodoctor(rs.getString("apellidodoctor"));
            return diagnostico;
        };
    }

}
