package com.habimed.habimedWebService.especialidad.application;

import java.util.List;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadService;
import com.habimed.habimedWebService.especialidad.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> getAllEspecialidades() {
        try {
            List<Especialidad> especialidades = especialidadService.findAll();
            return ResponseEntity.ok(especialidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Especialidad>> getEspecialidadesWithFilter(@Valid @RequestBody EspecialidadFilterDto filterDto) {
        try {
            List<Especialidad> especialidades = especialidadService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(especialidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDto> getEspecialidadById(@PathVariable Integer id) {
        try {
            EspecialidadResponseDto especialidad = especialidadService.getById(id);
            if (especialidad != null) {
                return ResponseEntity.ok(especialidad);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDto> createEspecialidad(@Valid @RequestBody EspecialidadInsertDto especialidadInsertDto) {
        try {
            EspecialidadResponseDto createdEspecialidad = especialidadService.save(especialidadInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEspecialidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EspecialidadResponseDto> updateEspecialidad(
            @PathVariable Integer id,
            @Valid @RequestBody EspecialidadUpdateDto especialidadUpdateDto) {
        try {
            EspecialidadResponseDto updatedEspecialidad = especialidadService.update(id, especialidadUpdateDto);
            if (updatedEspecialidad != null) {
                return ResponseEntity.ok(updatedEspecialidad);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEspecialidad(@PathVariable Integer id) {
        try {
            Boolean deleted = especialidadService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}