package com.habimed.habimedWebService.cita.application;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.CitaResponseDto;
import com.habimed.habimedWebService.cita.dto.CitaUpdateDto;
import com.habimed.habimedWebService.diagnostico.domain.service.DiagnosticoService;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoResponseDto;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.parameterREST.ResponseREST;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.cita.domain.service.CitaService;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.parameterREST.PeticionREST;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/citas")
@RequiredArgsConstructor
public class CitaController {
    
    private final CitaService citaService;
    /*
    * Segun el tipo de usuario se puede mostrar sus determanadas citas
    * Eso ya con seguridad*/
    @GetMapping("/me")
    public ResponseEntity<List<CitaResponseDto>> getCitasPaciente(@RequestBody CitaRequest request) {       // No se para qué el req body
        List<CitaResponseDto> citas = citaService.getCitas(request);
        return ResponseEntity.ok(citas);
    }

    /* Citas en las que aparece determinado usuario por id */
    @GetMapping("/me/{id}")
    public ResponseEntity<CitaResponseDto> getCitaById(@PathVariable Integer id) {
        try {
            CitaResponseDto cita = citaService.getCitaById(id);
            return ResponseEntity.ok(cita);
        }catch (Exception e){
            throw new ResourceNotFoundException("No se encontró una cita con el id " + id);
        }
    }

    /* Crear una cita*/
    @PostMapping
    public ResponseEntity<Integer> addCita(@RequestBody CitaRequest citaRequest) {
        try {
            Integer idCita = citaService.setCita(citaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(idCita);
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /* Actualizar una cita */
    @PatchMapping("/update/{id}")
    public ResponseEntity<CitaResponseDto> updateCita(@PathVariable Integer id, @Valid @RequestBody CitaUpdateDto citaUpdateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(citaService.updateCita(id, citaUpdateDto));
    }

    /* Eliminar una cita */
    @DeleteMapping("deleteCita")
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
