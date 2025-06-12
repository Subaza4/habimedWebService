package com.habimed.habimedWebService.consultorio.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorioTieneServicio.domain.model.ConsultorioTieneServicio;
import com.habimed.habimedWebService.consultorioTieneServicio.domain.service.ConsultorioTieneServicioService;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service.DoctorTrabajaConsultorioService;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioResponseDto;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.parameterREST.ResponseREST;
import jakarta.validation.Valid;
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
public class ConsultorioController {

    private final ConsultorioService consultorioService;
    private final ModelMapper modelMapper;
    private final ConsultorioTieneServicioService consultorioTieneServicioService;
    private final DoctorTrabajaConsultorioService doctorTrabajaConsultorioService;

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
    public ResponseEntity<Integer> createOrUpdateConsultorio(@Valid @RequestBody(required = false) ConsultorioRequest request) {
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

    /* Obtener todos los consultorios con sus servicios*/
    @GetMapping("/servicio")
    public ResponseEntity<List<ConsultorioTieneServicio>> getAllConsultoriosServicios(@Valid @RequestBody ConsultorioTieneServicioRequest request) {
        try{
            List<ConsultorioTieneServicio> consultoriosServicios = consultorioTieneServicioService.getAllConsultoriosServicios(request);
            //response.setSalidaMsg("Consultorios y servicios obtenidos correctamente.");
            return ResponseEntity.ok(consultoriosServicios);
        } catch (Exception e) {
            //response.setSalidaMsg("Error al obtener los consultorios y servicios.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener consultorios y servicios", e);
        }
    }

    /* Obtener los servicios de un consultorio */
    @GetMapping("/servicio/{idConsultorio}")
    public ResponseEntity<List<ConsultorioTieneServicio>> getServiciosByConsultorio(@PathVariable Integer idConsultorio) {
        try{
            List<ConsultorioTieneServicio> consultoriosServicios = consultorioTieneServicioService.getConsultoriosServiciosByIdConsultorio(idConsultorio);
            return ResponseEntity.ok(consultoriosServicios);
        } catch (Exception e) {
            //response.setSalidaMsg("Error al obtener los consultorios y servicios.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los servicios del consultorio: " + idConsultorio, e);
        }
    }

    /* Agregar un servicio a un consultorio (el idConsultorio se manda por el body) */
    @PostMapping("/servicio")
    public ResponseEntity<ConsultorioTieneServicio> addConsultorioTieneServicio(@Valid @RequestBody ConsultorioTieneServicioRequest request) {
        try {
            ConsultorioTieneServicio appended = consultorioTieneServicioService.addConsultorioTieneServicio(request);
            return ResponseEntity.ok(appended);

        } catch (Exception e) {
            //response.setSalidaMsg("Error al procesar la solicitud");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al agregar una relación consultorio y servicio", e);
        }
    }

    /* Eliminar servicios de un consultorio */
    @DeleteMapping("/servicio/{id}")
    public ResponseEntity<Boolean> deleteConsultorioTieneServicio(@Valid @RequestBody ConsultorioTieneServicioRequest request) {
        try{
            Boolean result = consultorioTieneServicioService.deleteConsultorioTieneServicio(request);
            return ResponseEntity.ok(result);

        }catch (Exception e) {
            //response.setSalidaMsg("Error al eliminar el consultorio y servicio.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar la relación consultorio y servicio", e);
        }
    }


    /* Obtener los profecionales que trabajan en un consultorio */
    @GetMapping("/doctor")
    public ResponseEntity<List<DoctorTrabajaConsultorioDTO>> getDoctores(@Valid @RequestBody DoctorTrabajaConsultorioRequest request) {
        try{
            List<DoctorTrabajaConsultorioDTO> doctores = doctorTrabajaConsultorioService.getAllDoctorsTrabajaConsultorioWithDetails(request);
            return ResponseEntity.ok(doctores);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener a los doctores del consultorio", e);
        }
    }

    /* Agregar profecionales como trabajadores de un consultorio */
    @PostMapping("/doctor")
    public ResponseEntity<DoctorTrabajaConsultorioResponseDto> addDoctor(@Valid @RequestBody DoctorTrabajaConsultorioRequest request) {
        try {
            DoctorTrabajaConsultorioResponseDto resultado = doctorTrabajaConsultorioService.addDoctorTrabajaConsultorio(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al añadir al doctor como trabajador", e);
        }
    }

    /* Eliminar profecionales del consultorio (el IdDoctor se envía en el body) */
    @DeleteMapping("/doctor")
    public ResponseEntity<Boolean> deleteDoctor(@RequestBody DoctorTrabajaConsultorioRequest request) {
        try {
            Boolean isDeleted = doctorTrabajaConsultorioService.deleteDoctorTrabajaConsultorio(request);
            return ResponseEntity.ok(isDeleted);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar la relación consultorio y doctor", e);
        }
    }


}





