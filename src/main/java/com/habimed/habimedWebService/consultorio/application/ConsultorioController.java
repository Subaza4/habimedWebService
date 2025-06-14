package com.habimed.habimedWebService.consultorio.application;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.consultorio.domain.service.ConsultorioService;

import java.util.List;


@RestController
@RequestMapping("/api/consultorios")
@RequiredArgsConstructor
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    @GetMapping
    public ResponseEntity<List<Consultorio>> getAllConsultorios() {
        List<Consultorio> consultorios = consultorioService.findAll();
        return ResponseEntity.ok(consultorios);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Consultorio>> getAllConsultoriosWithConditions(@RequestBody ConsultorioFilterDto consultorioFilterDto) {
        List<Consultorio> consultorios = consultorioService.findAllWithConditions(consultorioFilterDto);
        return ResponseEntity.ok(consultorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultorioResponseDto> getConsultorioById(@PathVariable Integer id) {
        ConsultorioResponseDto consultorio = consultorioService.getById(id);
        return ResponseEntity.ok(consultorio);
    }

    @PostMapping
    public ResponseEntity<ConsultorioResponseDto> createConsultorio(@Valid @RequestBody ConsultorioInsertDto consultorioInsertDto) {
        ConsultorioResponseDto consultorioCreado = consultorioService.save(consultorioInsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultorioCreado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConsultorioResponseDto> updateConsultorio(
            @PathVariable Integer id,
            @Valid @RequestBody ConsultorioUpdateDto consultorioUpdateDto) {
        ConsultorioResponseDto consultorioActualizado = consultorioService.update(id, consultorioUpdateDto);
        return ResponseEntity.ok(consultorioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultorio(@PathVariable Integer id) {
        Boolean deleted = consultorioService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}