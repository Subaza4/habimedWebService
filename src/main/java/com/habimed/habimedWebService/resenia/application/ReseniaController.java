package com.habimed.habimedWebService.resenia.application;

import com.habimed.habimedWebService.resenia.domain.model.Resenia;
import com.habimed.habimedWebService.resenia.domain.service.ReseniaService;
import com.habimed.habimedWebService.resenia.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenias")
@RequiredArgsConstructor
public class ReseniaController {

    final private ReseniaService reseniaService;

    @GetMapping
    public ResponseEntity<List<Resenia>> getAllResenias() {
        try {
            List<Resenia> resenias = reseniaService.findAll();
            return ResponseEntity.ok(resenias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Resenia>> getReseniasWithFilter(@Valid @RequestBody ReseniaFilterDto filterDto) {
        try {
            List<Resenia> resenias = reseniaService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(resenias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReseniaResponseDto> getReseniaById(@PathVariable Integer id) {
        try {
            ReseniaResponseDto resenia = reseniaService.getById(id);
            if (resenia != null) {
                return ResponseEntity.ok(resenia);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ReseniaResponseDto> createResenia(@Valid @RequestBody ReseniaInsertDto reseniaInsertDto) {
        try {
            ReseniaResponseDto createdResenia = reseniaService.save(reseniaInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResenia);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReseniaResponseDto> updateResenia(
            @PathVariable Integer id,
            @Valid @RequestBody ReseniaUpdateDto reseniaUpdateDto) {
        try {
            ReseniaResponseDto updatedResenia = reseniaService.update(id, reseniaUpdateDto);
            if (updatedResenia != null) {
                return ResponseEntity.ok(updatedResenia);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResenia(@PathVariable Integer id) {
        try {
            Boolean deleted = reseniaService.delete(id);
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
