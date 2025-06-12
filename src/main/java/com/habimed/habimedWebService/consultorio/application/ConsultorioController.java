package com.habimed.habimedWebService.consultorio.application;

import java.util.ArrayList;
import java.util.List;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.parameterREST.ResponseREST;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.consultorio.domain.service.ConsultorioService;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;
import com.habimed.parameterREST.PeticionREST;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/consultorio")
@RequiredArgsConstructor
public class ConsultorioController extends PeticionREST {

    private final ConsultorioService consultorioService;
    private final ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<List<ConsultorioDTO>> getAllConsultorios(@RequestBody(required = false) ConsultorioRequest request) {
        try{
            List<ConsultorioDTO> consultorios = consultorioService.getAllConsultorios(request);
            return ResponseEntity.ok(consultorios);

        } catch (Exception e) {
            // Otras excepciones pueden manejarse de otra forma.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener consultorios", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultorioDTO> getConsultorio(@PathVariable Integer id) {
        ResponseREST response = new ResponseREST();
        try{
            ConsultorioDTO consultorio = consultorioService.getConsultorioById(id);
            return ResponseEntity.ok(consultorio);
        } catch (Exception e) {
            // Otras excepciones pueden manejarse de otra forma.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener consultorios", e);
        }
    }

    @PostMapping
    public ResponseEntity<Integer> createOrUpdateConsultorio(@RequestBody(required = false) ConsultorioRequest request) {
        try{
            Integer consultorio = consultorioService.setConsultorio(request);
            if (consultorio == 0) {
                //response.setSalidaMsg("No se pudo crear el consultorio.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(consultorio);
            } else if(consultorio == 1){
                //response.setSalidaMsg("Consultorio creado correctamente.");
                return ResponseEntity.status(HttpStatus.CREATED).body(consultorio);
            }else if(consultorio == 2){
                //response.setSalidaMsg("Consultorio actualizado correctamente.");
                return ResponseEntity.status(HttpStatus.OK).body(consultorio);
            }else{
                //response.setSalidaMsg("Error al guardar los datos del consultorio.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(consultorio);
            }
        }catch (Exception e) {
            // Otras excepciones pueden manejarse de otra forma.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener consultorios", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteConsultorio(@PathVariable Integer id) {
        try {
            Boolean isDeleted = consultorioService.deleteConsultorio(id);

            if(isDeleted) {
                //response.setSalidaMsg("Consultorio eliminado correctamente.");
                return ResponseEntity.status(HttpStatus.OK).body(isDeleted);
            } else {
                //response.setSalidaMsg("No se encontró el consultorio o no pudo ser eliminado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(isDeleted);
            }
        } catch (Exception e) {
            // response.setSalidaMsg("Error al eliminar el consultorio: " + e.getMessage());
            // Otras excepciones pueden manejarse de otra forma.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el consultorio", e);
        }
    }

}





