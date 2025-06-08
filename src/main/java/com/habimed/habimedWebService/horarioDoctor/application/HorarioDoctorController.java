package com.habimed.habimedWebService.horarioDoctor.application;

import com.habimed.habimedWebService.horarioDoctor.domain.service.HorarioDoctorService;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorDTO;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorRequest;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponseREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/consultorio")
public class HorarioDoctorController extends PeticionREST {

    private final HorarioDoctorService horarioDoctorService;

    @Autowired
    public HorarioDoctorController(HorarioDoctorService horarioDoctorService) {
        this.horarioDoctorService = horarioDoctorService;
    }

    @PostMapping("/getHorariosDoctor")
    public ResponseEntity<ResponseREST> getHorariosDoctor(@RequestBody HorarioDoctorRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<HorarioDoctorDTO> lineas = horarioDoctorService.getAllHorarios(request);
            if(lineas.isEmpty()) {
                response.setStatus(STATUS_OK);
                response.setSalida(lineas);
                response.setSalidaMsg("Horarios del doctor obtenidos correctamente");
            }else{
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("No se encontraron horarios del doctor");
                response.setSalida(lineas);
            }
        }catch(Exception e){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener los horarios del doctor");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/setHorariosDoctor")
    public ResponseEntity<ResponseREST> setHorariosDoctor(@RequestBody HorarioDoctorRequest request){
        ResponseREST response = new ResponseREST();
        try{
            Integer resultado = this.horarioDoctorService.setHorario(request);
            if(resultado == 0) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error: El horario se solapa con otro existente para este doctor");
            } else if(resultado == 1) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Horario agregado correctamente");
            } else if (resultado == 2) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Horario actualizado correctamente");
            } else if(resultado == 3){
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error de solapamiento de horarios");
            }
        }catch(Exception e){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar el horario del doctor");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteHorariosDoctor")
    public ResponseEntity<ResponseREST> deleteHorariosDoctor(@RequestBody HorarioDoctorRequest request){
        ResponseREST response = new ResponseREST();
        try{
            Boolean isDeleted = this.horarioDoctorService.deleteHorario(request.getIdhorariodoctor());
            if(isDeleted) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Horario del doctor eliminado correctamente");
            }else{
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se pudo eliminar el horario del doctor");
            }
        }catch(Exception e){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener los horarios del doctor");
        }
        return ResponseEntity.ok(response);
    }
}