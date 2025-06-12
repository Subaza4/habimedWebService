package com.habimed.habimedWebService.doctorTrabajaConsultorio.application;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.parameterREST.ResponseREST;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service.DoctorTrabajaConsultorioService;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.parameterREST.PeticionREST;

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
    public ResponseEntity<ResponseREST> getDoctoresTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request)
    {
        ResponseREST response = new ResponseREST();
        List<DoctorTrabajaConsultorioDTO> doctores = doctorTrabajaConsultorioService.getAllDoctorsTrabajaConsultorio(request);
        if(doctores.isEmpty()) {
            response.setSalidaMsg("No se encontraron doctores que trabajen en el consultorio");
            response.setStatus(STATUS_KO);
        }else{
            response.setStatus(STATUS_OK);
            response.setSalida(doctores);
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("setDoctorTrabajaConsultorio")
    public ResponseEntity<ResponseREST> setDoctorTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request)
    {
        ResponseREST response = new ResponseREST();
        try {
            Integer resultado = doctorTrabajaConsultorioService.setDoctorTrabajaConsultorio(request);
            switch (resultado) {
                case 1:
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("Doctor asignado al consultorio correctamente");
                    break;
                case 2:
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("La asignación del doctor al consultorio ya existe");
                    break;
                default:
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("Error al asignar el doctor al consultorio");
                    break;
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar la asignación del doctor al consultorio");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("deleteDoctorTrabajaConsultorio")
    public ResponseEntity<ResponseREST> deleteDoctorTrabajaConsultorio(@RequestBody DoctorTrabajaConsultorioRequest request)
    {
        ResponseREST response = new ResponseREST();
        try{
            Boolean isDeleted = doctorTrabajaConsultorioService.deleteDoctorTrabajaConsultorio(request);
            if(isDeleted) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Doctor eliminado del consultorio correctamente");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se pudo elimiar el doctor del consultorio");
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar el doctor del consultorio");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}