package com.habimed.habimedWebService.consultorioTieneServicio.application;

import com.habimed.parameterREST.ResponseREST;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.consultorioTieneServicio.domain.service.ConsultorioTieneServicioService;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import com.habimed.parameterREST.PeticionREST;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/consultorioServicio")
public class ConsultorioTieneServicioController extends PeticionREST{
    
    private final ConsultorioTieneServicioService consultorioTieneServicioService;

    @Autowired
    public ConsultorioTieneServicioController(ConsultorioTieneServicioService consultorioTieneServicioService) {
        this.consultorioTieneServicioService = consultorioTieneServicioService;
    }

    @PostMapping("/getAllConsultoriosServicios")
    public ResponseEntity<ResponseREST> getAllConsultoriosServicios(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<ConsultorioTieneServicioDTO> consultoriosServicios = consultorioTieneServicioService.getAllConsultoriosServicios(request);
            if(consultoriosServicios != null && !consultoriosServicios.isEmpty()) {
                response.setSalida(consultoriosServicios);
                response.setSalidaMsg("Consultorios y servicios obtenidos correctamente.");
                response.setStatus(STATUS_OK);
            } else {
                response.setSalidaMsg("No se encontraron consultorios y servicios.");
                response.setStatus(STATUS_KO);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener los consultorios y servicios.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
 
    /*@PostMapping("/getConsultorioServicio")
    public ResponseEntity<ResponseREST> getConsultoriosServiciosById(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            ConsultorioTieneServicioDTO consultorioServicio = consultorioTieneServicioService.getConsultoriosServiciosById(request);
            if (consultorioServicio != null) {
                response.setSalida(consultorioServicio);
                response.setSalidaMsg("Consultorio y servicio obtenido correctamente.");
                response.setStatus(STATUS_OK);
            } else {
                response.setSalidaMsg("No se encontró el consultorio y servicio con el ID proporcionado.");
                response.setStatus(STATUS_KO);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener el consultorio y servicio con el ID proporcionado.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }*/

    @PostMapping("/setConsultorioTieneServicio")
    public ResponseEntity<ResponseREST> setConsultorioTieneServicio(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try {
            Integer resultado = consultorioTieneServicioService.setConsultorioTieneServicio(request);
            Map<String, Object> salidaMap = new HashMap<>();
            salidaMap.put("resultado", resultado);
            switch (resultado) {
                case 0:
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("Error: Parámetros inválidos o error en la operación.");
                    response.setSalida(salidaMap);
                    break;
                case 1:
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("Servicio asignado al consultorio exitosamente.");
                    response.setSalida(salidaMap);
                    break;
                case 3:
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("El servicio ya está asignado a este consultorio.");
                    response.setSalida(salidaMap);
                    break;
                default:
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("Error desconocido al procesar la solicitud.");
                    response.setSalida(salidaMap);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar la solicitud");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteConsultorioTieneServicio")
    public ResponseEntity<ResponseREST> deleteConsultorioTieneServicio(@RequestBody ConsultorioTieneServicioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            boolean result = consultorioTieneServicioService.deleteConsultorioTieneServicio(request);
            if (result) {
                response.setSalidaMsg("Consultorio y servicio eliminados correctamente.");
                response.setStatus(STATUS_OK);
            } else {
                response.setSalidaMsg("Error al eliminar el consultorio y servicio.");
                response.setStatus(STATUS_KO);
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar el consultorio y servicio.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}