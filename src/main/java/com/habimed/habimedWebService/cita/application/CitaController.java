package com.habimed.habimedWebService.cita.application;

import java.util.List;

import com.habimed.parameterREST.ResponseREST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.cita.domain.service.CitaService;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.parameterREST.PeticionREST;

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
    public ResponseEntity<ResponseREST> getCitas(@RequestBody CitaRequest request) {
        ResponseREST responseREST = new ResponseREST();
        List<CitaDTO> citas = citaService.getCitas(request);
        if (citas.isEmpty()) {
            responseREST.setStatus(STATUS_KO);
            responseREST.setSalidaMsg("No se encontraron citas.");
        } else {
            responseREST.setStatus(STATUS_OK);
            responseREST.setSalidaMsg("Citas encontradas exitosamente.");
            responseREST.setSalida(citas);
        }

        return ResponseEntity.ok(responseREST);
    }

    @PostMapping("getCita")
    public ResponseEntity<ResponseREST> getCita(@RequestBody CitaRequest request) {
        ResponseREST responseREST = new ResponseREST();
        CitaDTO cita = citaService.getCita(request.getIdcita());
        if (cita == null) {
            responseREST.setStatus(STATUS_KO);
            responseREST.setSalidaMsg("Cita no encontrada.");
        } else {
            responseREST.setStatus(STATUS_OK);
            responseREST.setSalidaMsg("Cita encontrada exitosamente.");
            responseREST.setSalida(cita);
        }

        return ResponseEntity.ok(responseREST);
    }
    
    @PostMapping("setCita")
    public ResponseEntity<ResponseREST> setCita(@RequestBody CitaRequest citaRequest) {
        ResponseREST responseREST = new ResponseREST();
        Integer idCita = citaService.setCita(citaRequest);
        if (idCita == null) {
            responseREST.setStatus(STATUS_KO);
            responseREST.setSalidaMsg("Error al crear la cita.");
        } else {
            responseREST.setStatus(STATUS_OK);
            responseREST.setSalidaMsg("Cita creada exitosamente.");
            responseREST.setSalida(idCita);
        }

        return ResponseEntity.ok(responseREST);
    }

    @PostMapping("deleteCita")
    public ResponseEntity<ResponseREST> deleteCita(@RequestBody CitaRequest request) {
        ResponseREST responseREST = new ResponseREST();
        try {
            boolean deleted = citaService.deleteCita(request);
            if (deleted) {
                responseREST.setStatus(STATUS_OK);
                responseREST.setSalidaMsg("Cita eliminada exitosamente.");
            } else {
                responseREST.setStatus(STATUS_KO);
                responseREST.setSalidaMsg("Error al eliminar la cita.");
            }
        }catch (Exception e) {
            responseREST.setStatus(STATUS_KO);
            responseREST.setSalidaMsg("Error al eliminar la cita.");
            responseREST.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(responseREST);
    }
}
