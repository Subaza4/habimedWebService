package com.habimed.habimedWebService.consultorio.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.consultorio.domain.parameter.consultorioRequest.ConsultorioRequest;
import com.habimed.habimedWebService.consultorio.domain.service.ConsultorioService;
import com.habimed.habimedWebService.consultorio.repository.dto.ConsultorioDTO;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

@RestController
@RequestMapping("/consultorio")
public class ConsultorioController extends PeticionREST {

    private final ConsultorioService consultorioService;

    public ConsultorioController(ConsultorioService consultorioService) {
        this.consultorioService = consultorioService;
    }


    @PostMapping("/getConsultorios")
    public ResponseEntity<ResponsePRES> getAllConsultorios(@RequestBody(required = false) ConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        List<ConsultorioDTO> consultorios = consultorioService.getAllConsultorios(request);

        if (consultorios.isEmpty()) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontraron consultorios.");
        } else {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Consultorios encontrados exitosamente.");
            response.setSalida(consultorios);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/getConsultorio")
    public ResponseEntity<ResponsePRES> getConsultorio(@RequestBody(required = false) ConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        ConsultorioDTO consultorio = consultorioService.getConsultorioById(request.getIdconsultorio());

        if (consultorio == null) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontró el consultorio.");
            response.setSalida(new ArrayList<>());
        } else {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Consultorio encontrado.");
            response.setSalida(consultorio);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/setConsultorio")
    public ResponseEntity<ResponsePRES> setConsultorio(@RequestBody(required = false) ConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        Integer consultorio = consultorioService.setConsultorio(request);

        if (consultorio == 0) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se pudo crear el consultorio.");
        } else if(consultorio == 1){
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Consultorio guardado correctamente.");
        }else if(consultorio == 2){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("El consultorio ya existe.");
        }else{
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al guardar los datos del consultorio.");
            response.setSalida(consultorio);
        }

        return ResponseEntity.ok(response);
    }
}
