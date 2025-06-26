package com.habimed.habimedWebService.servicio.application;

import com.habimed.habimedWebService.servicio.dto.*;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.habimed.habimedWebService.servicio.domain.service.ServicioService;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    final private ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<Servicio>> getAllServicios() {
        try {
            List<Servicio> servicios = servicioService.findAll();
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Servicio>> getServiciosWithFilter(@Valid @RequestBody ServicioFilterDto filterDto) {
        try {
            List<Servicio> servicios = servicioService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(servicios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponseDto> getServicioById(@PathVariable Integer id) {
        try {
            ServicioResponseDto servicio = servicioService.getById(id);
            if (servicio != null) {
                return ResponseEntity.ok(servicio);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ServicioResponseDto> createServicio(@Valid @RequestBody ServicioInsertDto servicioInsertDto) {
        try {
            ServicioResponseDto createdServicio = servicioService.save(servicioInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdServicio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponseDto> updateServicio(
            @PathVariable Integer id,
            @Valid @RequestBody ServicioUpdateDto servicioUpdateDto) {
        try {
            ServicioResponseDto updatedServicio = servicioService.update(id, servicioUpdateDto);
            if (updatedServicio != null) {
                return ResponseEntity.ok(updatedServicio);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicio(@PathVariable Integer id) {
        try {
            Boolean deleted = servicioService.delete(id);
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