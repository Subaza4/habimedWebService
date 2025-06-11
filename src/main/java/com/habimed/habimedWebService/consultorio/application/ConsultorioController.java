package com.habimed.habimedWebService.consultorio.application;

import java.util.ArrayList;
import java.util.List;

import com.habimed.parameterREST.ResponseREST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.consultorio.domain.service.ConsultorioService;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;
import com.habimed.parameterREST.PeticionREST;

@RestController
@RequestMapping("/consultorio")
public class ConsultorioController extends PeticionREST {

    private final ConsultorioService consultorioService;

    public ConsultorioController(ConsultorioService consultorioService) {
        this.consultorioService = consultorioService;
    }


    @PostMapping("/getConsultorios")
    public ResponseEntity<ResponseREST> getAllConsultorios(@RequestBody(required = false) ConsultorioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<ConsultorioDTO> consultorios = consultorioService.getAllConsultorios(request);
            if (consultorios.isEmpty()) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontraron consultorios.");
            } else {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Consultorios encontrados exitosamente.");
                response.setSalida(consultorios);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener los consultorios.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getConsultorio")
    public ResponseEntity<ResponseREST> getConsultorio(@RequestBody(required = false) ConsultorioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            ConsultorioDTO consultorio = consultorioService.getConsultorioById(request);
            if (consultorio == null) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontró el consultorio.");
                response.setSalida(new ArrayList<>());
            } else {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Consultorio encontrado.");
                response.setSalida(consultorio);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener el consultorio.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/setConsultorio")
    public ResponseEntity<ResponseREST> setConsultorio(@RequestBody(required = false) ConsultorioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            Integer consultorio = consultorioService.setConsultorio(request);
            if (consultorio == 0) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se pudo crear el consultorio.");
            } else if(consultorio == 1){
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Consultorio creado correctamente.");
            }else if(consultorio == 2){
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Consultorio actualizado correctamente.");
            }else{
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error al guardar los datos del consultorio.");
                response.setSalida(consultorio);
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al crear el consultorio.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteConsultorio")
    public ResponseEntity<ResponseREST> deleteConsultorio(@RequestBody ConsultorioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            Boolean isDeleted = consultorioService.deleteConsultorio(request);
            if(isDeleted) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Consultorio eliminado correctamente.");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se pudo eliminar el consultorio.");
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar el consultorio.");
        }
        return ResponseEntity.ok(response);
    }
}
