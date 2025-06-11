package com.habimed.habimedWebService.cita.application;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.CitaResponseDto;
import com.habimed.habimedWebService.cita.dto.CitaUpdateDto;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.parameterREST.ResponseREST;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.cita.domain.service.CitaService;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.parameterREST.PeticionREST;


@RestController
@RequestMapping("/citas")
public class CitaController extends PeticionREST {
    
    private final CitaService citaService;
    
    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    /*
    * Segun el tipo de usuario se puede mostrar sus determanadas citas
    * Eso ya con seguridad*/
    @GetMapping("/me")
    public ResponseEntity<List<CitaDTO>> getCitasPaciente(@RequestBody CitaRequest request) {       // No se para qué el req body
        List<CitaDTO> citas = citaService.getCitas(request);
        return ResponseEntity.ok(citas);
    }

    /* Citas en las que aparece determinado usuario por id */
    @GetMapping("/me/{id}")
    public ResponseEntity<CitaDTO> getCitaById(@PathVariable Integer id) {
        CitaDTO cita = citaService.getCitaById(id);
        return ResponseEntity.ok(cita);
    }

    /* Crear una cita*/
    @PostMapping
    public ResponseEntity<Integer> setCita(@RequestBody CitaRequest citaRequest) {
        Integer idCita = citaService.setCita(citaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(idCita);
    }

    /* Actualizar una cita */
    @PatchMapping("/update/{id}")
    public ResponseEntity<CitaResponseDto> updateCita(@PathVariable Integer id, @Valid @RequestBody CitaUpdateDto citaUpdateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(citaService.updateCita(id, citaUpdateDto));
    }

    /* Eliminar una cita */
    @PostMapping("deleteCita")
    public ResponseEntity<Boolean> deleteCita(@RequestBody CitaRequest request) {
        ResponseREST responseREST = new ResponseREST();
        try {
            Boolean deleted = citaService.deleteCita(request);
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        }catch (Exception e) {
            throw new ResourceNotFoundException("Nose puede eliminar una cita que no existe.");
        }

    }
}
