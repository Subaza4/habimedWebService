package com.habimed.habimedWebService.permisoHistorial.domain.service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialDTO;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialRequest;
import com.habimed.habimedWebService.permisoHistorial.repository.PermisoHistorialRepository;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioServiceImpl;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermisoHistorialServiceImpl implements PermisoHistorialService{
    private PermisoHistorialRepository permisoHistorialRepository;
    private UsuarioService usuarioService;

    @Autowired
    public PermisoHistorialServiceImpl(PermisoHistorialRepository permisoHistorialRepository) {
        this.permisoHistorialRepository = permisoHistorialRepository;
    }

    @Override
    public List<PermisoHistorialDTO> getPermisosHistorial(PermisoHistorialRequest request) {

        return null;
    }

    @Override
    public Integer setPermisoHistorial(PermisoHistorialRequest request) {

        return null;
    }

    @Override
    public Boolean deletePermisoHistorial(PermisoHistorialRequest request) {

        return null;
    }

    @Override
    public PermisoHistorialDTO getpermisoDoctor (PermisoHistorialRequest request) {
        Integer idDoctor = usuarioService.getUsuarioByToken(request.getToken()).getIdusuario();
        if(idDoctor != null && idDoctor > 0 && request.getIdpaciente() != null && request.getIdpaciente() > 0){
            return permisoHistorialRepository.getpermisoDoctor(idDoctor, request.getIdpaciente());
        }else{
            return new PermisoHistorialDTO();
        }
    }

    @Override
    public Consultorio getConsultoriosUsuario(PermisoHistorialRequest request) {
        return null;
    }

    @Override
    public List<Cita> getCitasPaciente(PermisoHistorialRequest request) {
        return List.of();
    }

    @Override
    public List<Receta> getRecetasPaciente(PermisoHistorialRequest request) {
        return List.of();
    }

    @Override
    public List<Recomendacion> getRecomendacionesPaciente(PermisoHistorialRequest request) {
        return List.of();
    }

    @Override
    public List<Diagnostico> getDiagnosticoPaciente(PermisoHistorialRequest request) {
        return List.of();
    }
}
