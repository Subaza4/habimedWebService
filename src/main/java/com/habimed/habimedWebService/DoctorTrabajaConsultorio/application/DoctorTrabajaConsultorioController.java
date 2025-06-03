package com.habimed.habimedWebService.doctorTrabajaConsultorio.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.parameter.request.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.parameter.response.DoctorTrabajaConsultorioResponse;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service.DoctorTrabajaConsultorioService;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/consultorio")
public class DoctorTrabajaConsultorioController extends PeticionREST{
    
    private final DoctorTrabajaConsultorioService doctorTrabajaConsultorioService;
    
    public DoctorTrabajaConsultorioController(DoctorTrabajaConsultorioService doctorTrabajaConsultorioService) {
        this.doctorTrabajaConsultorioService = doctorTrabajaConsultorioService;
    }

    @PostMapping("getDoctoresTrabajaConsultorio")
    public ResponseEntity<ResponsePRES> getDoctoresTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        List<DoctorTrabajaConsultorioResponse> doctores = doctorTrabajaConsultorioService.getAllDoctorsTrabajaConsultorio(request);
        if(doctores.isEmpty()) {
            response.setSalidaMsg("No se encontraron doctores que trabajen en el consultorio");
            response.setStatus(STATUS_KO);
        }else{
            response.setStatus(STATUS_OK);
            response.setSalida(doctores);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("getDoctorTrabajaConsultorio")
    public ResponseEntity<ResponsePRES> getDoctorTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        DoctorTrabajaConsultorioResponse doctor = doctorTrabajaConsultorioService.getDoctorTrabajaConsultorio(request);
        
        if(doctor != null && doctor.getIdDoctor() > 0) {
            response.setStatus(STATUS_OK);
            response.setSalida(doctor);
            response.setSalidaMsg("Doctor encontrado");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontró el doctor con ID: " + request.getIdDoctor());
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("setDoctorTrabajaConsultorio")
    public ResponseEntity<ResponsePRES> setDoctorTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        Integer isSaved = doctorTrabajaConsultorioService.createDoctorTrabajaConsultorio(request);
        
        if(isSaved > 0) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Doctor asignado al consultorio correctamente");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al asignar el doctor al consultorio");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("deleteDoctorTrabajaConsultorio")
    public ResponseEntity<ResponsePRES> deleteDoctorTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request) {
        ResponsePRES response = new ResponsePRES();
        boolean isDeleted = doctorTrabajaConsultorioService.deleteDoctorTrabajaConsultorio(request);
        
        if(isDeleted) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Doctor eliminado del consultorio correctamente");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar el doctor del consultorio");
        }
        
        return ResponseEntity.ok(response);
    }
}
