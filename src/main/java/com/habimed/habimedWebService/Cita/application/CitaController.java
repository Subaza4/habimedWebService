package com.habimed.habimedWebService.cita.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.cita.domain.service.CitaService;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaResponse;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/citas")
public class CitaController extends PeticionREST {
    
    private final CitaService citaService;
    
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping("getCitas")
    public ResponseEntity<ResponsePRES> getCitas(@RequestBody CitaRequest request) {
        ResponsePRES responsePRES = new ResponsePRES();
        List<CitaResponse> citas = citaService.getCitas(request);
        if (citas.isEmpty()) {
            responsePRES.setStatus(STATUS_KO);
            responsePRES.setSalidaMsg("No se encontraron citas.");
        } else {
            responsePRES.setStatus(STATUS_OK);
            responsePRES.setSalidaMsg("Citas encontradas exitosamente.");
            responsePRES.setSalida(citas);
        }

        return ResponseEntity.ok(responsePRES);
    }

    @PostMapping("getCita")
    public ResponseEntity<ResponsePRES> getCita(@RequestBody CitaRequest request) {
        ResponsePRES responsePRES = new ResponsePRES();
        CitaResponse cita = citaService.getCita(request.getIdcita());
        if (cita == null) {
            responsePRES.setStatus(STATUS_KO);
            responsePRES.setSalidaMsg("Cita no encontrada.");
        } else {
            responsePRES.setStatus(STATUS_OK);
            responsePRES.setSalidaMsg("Cita encontrada exitosamente.");
            responsePRES.setSalida(cita);
        }

        return ResponseEntity.ok(responsePRES);
    }
    
    @PostMapping("setCita")
    public ResponseEntity<ResponsePRES> setCita(@RequestBody CitaRequest citaRequest) {
        ResponsePRES responsePRES = new ResponsePRES();
        Integer idCita = citaService.setCita(citaRequest);
        if (idCita == null) {
            responsePRES.setStatus(STATUS_KO);
            responsePRES.setSalidaMsg("Error al crear la cita.");
        } else {
            responsePRES.setStatus(STATUS_OK);
            responsePRES.setSalidaMsg("Cita creada exitosamente.");
            responsePRES.setSalida(idCita);
        }

        return ResponseEntity.ok(responsePRES);
    }

    @PostMapping("deleteCita")
    public ResponseEntity<ResponsePRES> deleteCita(@RequestBody CitaRequest request) {
        ResponsePRES responsePRES = new ResponsePRES();
        boolean deleted = citaService.deleteCita(request.getIdcita());
        if (deleted) {
            responsePRES.setStatus(STATUS_OK);
            responsePRES.setSalidaMsg("Cita eliminada exitosamente.");
        } else {
            responsePRES.setStatus(STATUS_KO);
            responsePRES.setSalidaMsg("Error al eliminar la cita.");
        }

        return ResponseEntity.ok(responsePRES);
    }
}
