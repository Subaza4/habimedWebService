package com.habimed.habimedWebService.receta.application;

import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.receta.domain.service.RecetaService;
import com.habimed.habimedWebService.receta.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
public class RecetaController {

    final private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<Receta>> getAllRecetas() {
        try {
            List<Receta> recetas = recetaService.findAll();
            return ResponseEntity.ok(recetas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Receta>> getRecetasWithFilter(@Valid @RequestBody RecetaFilterDto filterDto) {
        try {
            List<Receta> recetas = recetaService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(recetas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDto> getRecetaById(@PathVariable Integer id) {
        try {
            RecetaResponseDto receta = recetaService.getById(id);
            if (receta != null) {
                return ResponseEntity.ok(receta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<RecetaResponseDto> createReceta(@Valid @RequestBody RecetaInsertDto recetaInsertDto) {
        try {
            RecetaResponseDto createdReceta = recetaService.save(recetaInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReceta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecetaResponseDto> updateReceta(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaUpdateDto recetaUpdateDto) {
        try {
            RecetaResponseDto updatedReceta = recetaService.update(id, recetaUpdateDto);
            if (updatedReceta != null) {
                return ResponseEntity.ok(updatedReceta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceta(@PathVariable Integer id) {
        try {
            Boolean deleted = recetaService.delete(id);
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
