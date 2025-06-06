package com.habimed.habimedWebService.consultorioTieneServicio.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.consultorioTieneServicio.domain.service.ConsultorioTieneServicioService;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/consultorio")
public class ConsultorioTieneServicioController extends PeticionREST{
    
    private final ConsultorioTieneServicioService consultorioTieneServicioService;

    @Autowired
    public ConsultorioTieneServicioController(ConsultorioTieneServicioService consultorioTieneServicioService) {
        this.consultorioTieneServicioService = consultorioTieneServicioService;
    }

    @PostMapping("/getAllConsultoriosServicios")
    public ResponseEntity<ResponsePRES> getAllConsultoriosServicios(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponsePRES response = new ResponsePRES();
        List<ConsultorioTieneServicioDTO> consultoriosServicios = consultorioTieneServicioService.getAllConsultoriosServicios(request);
        if(consultoriosServicios != null && !consultoriosServicios.isEmpty()) {
            response.setSalida(consultoriosServicios);
            response.setSalidaMsg("Consultorios y servicios obtenidos correctamente.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("No se encontraron consultorios y servicios.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }
 
    @PostMapping("/getConsultorioServicioById")
    public ResponseEntity<ResponsePRES> getConsultoriosServiciosById(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponsePRES response = new ResponsePRES();
        ConsultorioTieneServicioDTO consultorioServicio = consultorioTieneServicioService.getConsultoriosServiciosById(request);
        if (consultorioServicio != null) {
            response.setSalida(consultorioServicio);
            response.setSalidaMsg("Consultorio y servicio obtenido correctamente.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("No se encontró el consultorio y servicio con el ID proporcionado.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/setConsultorioTieneServicio")
    public ResponseEntity<ResponsePRES> setConsultorioTieneServicio(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponsePRES response = new ResponsePRES();
        Integer result = consultorioTieneServicioService.setConsultorioTieneServicio(request);
        if (result != null && result > 0) {
            response.setSalida(result);
            response.setSalidaMsg("Consultorio y servicio asignados correctamente.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("Error al asignar el consultorio y servicio.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteConsultorioTieneServicio")
    public ResponseEntity<ResponsePRES> deleteConsultorioTieneServicio(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponsePRES response = new ResponsePRES();
        boolean result = consultorioTieneServicioService.deleteConsultorioTieneServicio(request);
        if (result) {
            response.setSalidaMsg("Consultorio y servicio eliminados correctamente.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("Error al eliminar el consultorio y servicio.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }
}
