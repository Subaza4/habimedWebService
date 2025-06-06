package com.habimed.habimedWebService.especialidad.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadService;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadServiceImpl;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadRequest;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/especialidad")
public class EspecialidadController extends PeticionREST{

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadServiceImpl especialidadService) {
        this.especialidadService = especialidadService;
    }


    @PostMapping("/getEspecialidades")
    public ResponseEntity<ResponsePRES> getEspecialidades(@RequestBody @Valid EspecialidadRequest request) {
        ResponsePRES response = new ResponsePRES();
        List<Especialidad> especialidad = especialidadService.getEspecialidades();
        if(especialidad.isEmpty()) {
            response.setSalidaMsg("No se encontraron especialidades");
            response.setStatus(STATUS_KO);
        }else{
            response.setStatus(STATUS_OK);
            response.setSalida(especialidad);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("getEspecialidad")
    public ResponseEntity<ResponsePRES> getEspecialidad(@RequestBody String id) {
        ResponsePRES response = new ResponsePRES();
        Especialidad especialidad = especialidadService.getEspecialidad(Integer.parseInt(id));
        
        if(especialidad != null && especialidad.getIdespecialidad() != null) {
            response.setStatus(STATUS_OK);
            response.setSalida(especialidad);
            response.setSalidaMsg("Especialidad encontrada");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontró la especialidad con ID: " + id);

        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponsePRES> createEspecialidad(@RequestBody @Valid EspecialidadRequest request) {
        ResponsePRES response = new ResponsePRES();
        Integer idEspecilidad = especialidadService.setEspecialidad(request);
        if(idEspecilidad != null && idEspecilidad > 0) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Especialidad creada correctamente con ID: " + idEspecilidad);
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se pudo crear la especialidad");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponsePRES> updateEspecialidad(@RequestBody @Valid EspecialidadRequest request) {
        ResponsePRES response = new ResponsePRES();
        Integer idEspecialidad = especialidadService.setEspecialidad(request);

        if(idEspecialidad != null && idEspecialidad > 0) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Especialidad modificada correctamente");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se pudo modifcar la especialidad");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponsePRES> deleteEspecialidad(@RequestBody @Valid EspecialidadRequest request) {
        ResponsePRES response = new ResponsePRES();
        boolean isDeleted = especialidadService.deleteEspecialidad(request.getIdespecialidad());
        if(isDeleted) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Especialidad eliminada correctamente");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se pudo eliminar la especialidad");
        }
        return ResponseEntity.ok(response);
    }
}
