package com.habimed.habimedWebService.diagnostico.application;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.diagnostico.domain.service.DiagnosticoService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/diagnostico")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    /* Agregar un diagnóstico a la cita*/
    @PostMapping
    public ResponseEntity<DiagnosticoResponseDto> createDiagnostico(@RequestBody DiagnosticoRequest request) {
        DiagnosticoResponseDto diagnostico =  diagnosticoService.addDiagnostico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(diagnostico);
    }

    /* Obtener el diagnóstico de una cita con todos sus detalles */
    @GetMapping()
    public ResponseEntity<List<DiagnosticoDTO>> getDiagnosticosWithDetails(DiagnosticoRequest request) {
        List<DiagnosticoDTO> result = diagnosticoService.getAllDiagnosticosWithDetails(request);
        try {
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los diagnósticos", e);
        }
    }

    /* Obtener detalles de un diagnostico por su id */
    @GetMapping("/{idDiagnostico}")
    public ResponseEntity<DiagnosticoResponseDto> getDiagnosticoByIdDiagnostico(@PathVariable Integer idDiagnostico){
        return ResponseEntity.ok(diagnosticoService.getDiagnosticoByIdDiagnostico(idDiagnostico));
    }

    /* Obtener los diagnósticos para una cita */
    @GetMapping("/cita/{idCita}")
    public ResponseEntity<DiagnosticoResponseDto> getDiagnosticoByIdCita(@PathVariable Integer idCita){
        return ResponseEntity.ok(diagnosticoService.getDiagnosticoByIdDiagnostico(idCita));
    }

    /* Alterar algún campo de un diagnostico por su id */
    @PatchMapping("/{idDiagnostico}")
    public ResponseEntity<DiagnosticoResponseDto> updateDiagnostico(@PathVariable Integer idDiagnostico, @RequestBody DiagnosticoRequest request) {
        return ResponseEntity.ok(diagnosticoService.updateDiagnostico(idDiagnostico, request));
    }

    /*
      No se puede eliminar un diagnostico
     */
}
