package com.habimed.habimedWebService.recomendacion.application;


import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import com.habimed.habimedWebService.recomendacion.domain.service.RecomendacionService;
import com.habimed.habimedWebService.recomendacion.dto.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {

    final private RecomendacionService recomendacionService;

    @GetMapping
    public ResponseEntity<List<Recomendacion>> getAllRecomendaciones() {
        try {
            List<Recomendacion> recomendaciones = recomendacionService.findAll();
            return ResponseEntity.ok(recomendaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Recomendacion>> getRecomendacionesWithFilter(@Valid @RequestBody RecomendacionFilterDto filterDto) {
        try {
            List<Recomendacion> recomendaciones = recomendacionService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(recomendaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionResponseDto> getRecomendacionById(@PathVariable Integer id) {
        try {
            RecomendacionResponseDto recomendacion = recomendacionService.getById(id);
            if (recomendacion != null) {
                return ResponseEntity.ok(recomendacion);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<RecomendacionResponseDto> createRecomendacion(@Valid @RequestBody RecomendacionInsertDto recomendacionInsertDto) {
        try {
            RecomendacionResponseDto createdRecomendacion = recomendacionService.save(recomendacionInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecomendacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecomendacionResponseDto> updateRecomendacion(
            @PathVariable Integer id,
            @Valid @RequestBody RecomendacionUpdateDto recomendacionUpdateDto) {
        try {
            RecomendacionResponseDto updatedRecomendacion = recomendacionService.update(id, recomendacionUpdateDto);
            if (updatedRecomendacion != null) {
                return ResponseEntity.ok(updatedRecomendacion);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecomendacion(@PathVariable Integer id) {
        try {
            Boolean deleted = recomendacionService.delete(id);
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
