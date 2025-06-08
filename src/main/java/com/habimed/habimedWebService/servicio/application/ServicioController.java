package com.habimed.habimedWebService.servicio.application;

import com.habimed.habimedWebService.servicio.dto.ServicioDTO;
import com.habimed.parameterREST.ResponseREST;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.servicio.domain.service.ServicioService;
import com.habimed.habimedWebService.servicio.dto.ServicioRequest;
import com.habimed.parameterREST.PeticionREST;

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
    public ResponseEntity<ResponseREST> getServicios(@RequestBody ServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<ServicioDTO> servicios = servicioService.getAllServicios(request);
            if(servicios.isEmpty()) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontraron servicios");
            } else {
                response.setStatus(STATUS_OK);
                response.setSalida(servicios);
                response.setSalidaMsg("Servicios encontrados exitosamente.");
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar la solicitud.");
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("setServicio")
    public ResponseEntity<ResponseREST> setServicio(@RequestBody ServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            Integer respuesta = servicioService.setServicio(request);
            if(respuesta == 1) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("servicio creado exitosamente.");
            } else if(respuesta == 2) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("servicio actualizado exitosamente.");
            } else if(respuesta == 0) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Tipo de servicio no existe.");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error al procesar la solicitud.");
            }
        }catch (Exception e) {
            e.printStackTrace();
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar la solicitud.");
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("deleteServicio")
    public ResponseEntity<ResponseREST> deleteServicio(@RequestBody ServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            boolean resultado = servicioService.deleteServicio(request.getIdservicio());
            if(resultado) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Servicio eliminado exitosamente.");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se pudo eliminar el servicio.");
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar la solicitud.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
