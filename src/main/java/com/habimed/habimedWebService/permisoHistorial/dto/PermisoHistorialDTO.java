package com.habimed.habimedWebService.permisoHistorial.dto;

import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermisoHistorialDTO {
    private Integer idpermisohistorial;
    private Integer iddoctor;
    private Usuario doctor;
    private Integer idpaciente;
    private Usuario paciente;
    private String fechaotorgapermiso;
    private String fechadeniegapermiso;
    private Boolean estado = false;

    public RowMapper<PermisoHistorialDTO> permisoHistorialRowMapper() {

        return null;
    }
}
