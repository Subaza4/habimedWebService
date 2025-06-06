package com.habimed.habimedWebService.servicio.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.servicio.domain.service.ServicioService;
import com.habimed.habimedWebService.servicio.dto.ServicioRequest;
import com.habimed.habimedWebService.servicio.dto.ServicioResponse;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/servicio")
public class ServicioController extends PeticionREST{
    
    private ServicioService servicioService;
    
    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @PostMapping("/getServicios")
    public ResponseEntity<ResponsePRES> getServicios(@RequestBody ServicioRequest request) {
        ResponsePRES response = new ResponsePRES(); 
        List<ServicioResponse> servicios = servicioService.getAllServicios(request);
        if(servicios.isEmpty()) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontraron servicios");
        } else {
            response.setStatus(STATUS_OK);
            response.setSalida(servicios);
            response.setSalidaMsg("Servicios encontrados exitosamente.");
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("setServicio")
    public ResponseEntity<ResponsePRES> setServicio(@RequestBody ServicioRequest request) {
        ResponsePRES response = new ResponsePRES(); 
        Integer respuesta = servicioService.setServicio(request);
        if(respuesta == 1) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Servicio creado exitosamente.");
        } else if(respuesta == 2) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Servicio actualizado exitosamente.");
        } else if(respuesta == 0) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Tipo de servicio no existe.");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar la solicitud.");
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("deleteServicio")
    public ResponseEntity<ResponsePRES> deleteServicio(@RequestBody ServicioRequest request) {
        ResponsePRES response = new ResponsePRES(); 
        boolean resultado = servicioService.deleteServicio(request.getIdservicio());
        if(resultado) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Servicio eliminado exitosamente.");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar el servicio.");
        }
        return ResponseEntity.ok(response);
    }
}
