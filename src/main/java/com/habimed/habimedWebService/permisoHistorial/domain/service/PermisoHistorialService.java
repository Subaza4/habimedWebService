package com.habimed.habimedWebService.permisoHistorial.domain.service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialDTO;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialRequest;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;

import java.util.List;

public interface PermisoHistorialService {
    List<PermisoHistorialDTO> getPermisosHistorial(PermisoHistorialRequest request);

    Integer setPermisoHistorial(PermisoHistorialRequest request);

    Boolean deletePermisoHistorial(PermisoHistorialRequest request);

    PermisoHistorialDTO getpermisoDoctor (PermisoHistorialRequest request);

    Consultorio getConsultoriosUsuario (PermisoHistorialRequest request);

    List<Cita> getCitasPaciente (PermisoHistorialRequest request);

    List<Receta> getRecetasPaciente (PermisoHistorialRequest request);

    List<Recomendacion> getRecomendacionesPaciente (PermisoHistorialRequest request);

    List<Diagnostico> getDiagnosticoPaciente (PermisoHistorialRequest request);
}
