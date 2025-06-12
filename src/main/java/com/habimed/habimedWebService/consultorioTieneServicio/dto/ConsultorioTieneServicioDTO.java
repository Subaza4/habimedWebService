package com.habimed.habimedWebService.consultorioTieneServicio.dto;

import java.time.LocalDateTime;

import com.habimed.habimedWebService.consultorioTieneServicio.domain.model.ConsultorioTieneServicio;
import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultorioTieneServicioDTO {
    private Integer idCita;
    private Integer idPaciente;
    private Integer idDoctor;
    private String motivo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String estado;
    private String descripcion;

    public RowMapper<ConsultorioTieneServicioDTO> consultorioTieneServicioDTORowMapper(){
        return (rs, rowNum) -> {
            ConsultorioTieneServicioDTO dto = new ConsultorioTieneServicioDTO();
            dto.setIdCita(rs.getInt("idcita"));
            dto.setIdPaciente(rs.getInt("idpaciente"));
            dto.setIdDoctor(rs.getInt("iddoctor"));
            dto.setMotivo(rs.getString("motivo"));
            dto.setFechaHoraInicio(rs.getObject("fechahorainicio", LocalDateTime.class));
            dto.setFechaHoraFin(rs.getObject("fechahorafin", LocalDateTime.class));
            dto.setEstado(rs.getString("estado"));
            dto.setDescripcion(rs.getString("descripcion"));
            return dto;
        };
    }

    public RowMapper<ConsultorioTieneServicio> consultorioTieneServicioRowMapper(){
        return (rs, rowNum) -> {
            ConsultorioTieneServicio dto = new ConsultorioTieneServicio();
            dto.setIdconsultorio(rs.getInt("idconsultorio"));
            dto.setIdservicio(rs.getInt("idservicio"));

            return dto;
        };
    }
}
