package com.habimed.habimedWebService.especialidad.application;

import java.util.List;

import com.habimed.habimedWebService.especialidad.dto.EspecialidadResponseDto;
import com.habimed.parameterREST.ResponseREST;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadService;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadServiceImpl;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadRequest;
import com.habimed.parameterREST.PeticionREST;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/especialidad")
@RequiredArgsConstructor
public class EspecialidadController extends PeticionREST{

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<ResponseREST> getEspecialidades(@RequestBody @Valid EspecialidadRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<Especialidad> especialidad = especialidadService.getEspecialidades(request);
            if(especialidad.isEmpty()) {
                response.setSalidaMsg("No se encontraron especialidades");
                response.setStatus(STATUS_OK);
            }else{
                response.setStatus(STATUS_OK);
                response.setSalida(especialidad);
            }
        }catch(Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener las especialidades");
            response.setSalida(e.getMessage());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDto>> listarEspecialidades(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Boolean activa) {

        List<EspecialidadResponseDto> especialidades = especialidadService.listarEspecialidades(nombre, activa);
        return ResponseEntity.ok(especialidades);
    }

    @PostMapping("getEspecialidad")
    public ResponseEntity<ResponseREST> getEspecialidad(@RequestBody EspecialidadRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            if(request.getIdespecialidad() == null || request.getIdespecialidad() == 0) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Necesario campo id para obtener la especialidad");
            }else{
                Especialidad especialidad = especialidadService.getEspecialidad(request.getIdespecialidad());
                if(especialidad != null && especialidad.getIdEspecialidad() != null) {
                    response.setStatus(STATUS_OK);
                    response.setSalida(especialidad);
                } else {
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("No se encontró la especialidad con ID: " + request.getIdespecialidad());
                }
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener la especialidad");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/setEspecialidad")
    public ResponseEntity<ResponseREST> setEspecialidad(@RequestBody @Valid EspecialidadRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            Integer idEspecilidad = especialidadService.setEspecialidad(request);
            if(idEspecilidad == 1) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Especialidad creada correctamente");
            } else if(idEspecilidad == 2) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Especialidad actualizada correctamente");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error al guardar los datos de la especialidad");
                response.setSalida(idEspecilidad);
            }
        }catch(Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al crear la especialidad");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<ResponseREST> deleteEspecialidad(@RequestBody @Valid EspecialidadRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            if(request.getIdespecialidad() != null && request.getIdespecialidad() > 0) {
                if(especialidadService.deleteEspecialidad(request.getIdespecialidad())) {
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("Especialidad eliminada correctamente");
                } else {
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("No se pudo eliminar la especialidad");
                }
            }else{
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Necesario campo id para eliminar la especialidad");
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar la especialidad");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
