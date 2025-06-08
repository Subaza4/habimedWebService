package com.habimed.habimedWebService.doctorTrabajaConsultorio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTrabajaConsultorioDTO {
    private int idDoctor;
    private int idConsultorio;
    private String nombreDoctor;
    private String nombreConsultorio;
    private String apellidoDoctor;
    private String rucConsultorio;

    public RowMapper<DoctorTrabajaConsultorioDTO> doctorTrabajaConsultorioResponseRowMapper() {
        return (rs, rowNum) -> {
            DoctorTrabajaConsultorioDTO response = new DoctorTrabajaConsultorioDTO();
            response.setIdDoctor(rs.getInt("iddoctor")); 
            response.setIdConsultorio(rs.getInt("idconsultorio")); 
            response.setNombreDoctor(rs.getString("nombredoctor"));
            response.setNombreConsultorio(rs.getString("nombreconsultorio"));
            response.setApellidoDoctor(rs.getString("apellidodoctor"));
            response.setRucConsultorio(rs.getString("rucconsultorio"));
            return response;
        };
    }

}
