package com.habimed.habimedWebService.cita.application;

import java.util.List;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.cita.domain.service.CitaService;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> getAllCitas() {
        List<Cita> citas = citaService.findAll();
        return ResponseEntity.ok(citas);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Cita>> getAllCitasWithConditions(@RequestBody CitaFilterDto citaFilterDto) {
        List<Cita> citas = citaService.findAllWithConditions(citaFilterDto);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDto> getCitaById(@PathVariable Integer id) {
        CitaResponseDto cita = citaService.getById(id);
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDto> createCita(@Valid @RequestBody CitaInsertDto citaInsertDto) {
        CitaResponseDto citaCreada = citaService.save(citaInsertDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(citaCreada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CitaResponseDto> updateCita(
            @PathVariable Integer id,
            @Valid @RequestBody CitaUpdateDto citaUpdateDto) {
        CitaResponseDto citaActualizada = citaService.update(id, citaUpdateDto);
        return ResponseEntity.ok(citaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Integer id) {
        Boolean deleted = citaService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}