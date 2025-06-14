package com.habimed.habimedWebService.horarioDoctor.application;

import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import com.habimed.habimedWebService.horarioDoctor.domain.service.HorarioDoctorService;
import com.habimed.habimedWebService.horarioDoctor.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horario_Doctor")
@RequiredArgsConstructor
public class HorarioDoctorController {

    private final HorarioDoctorService horarioDoctorService;

    @GetMapping
    public ResponseEntity<List<HorarioDoctor>> getAllHorarioDoctor() {
        try {
            List<HorarioDoctor> horarioDoctorServiceAll = horarioDoctorService.findAll();
            return ResponseEntity.ok(horarioDoctorServiceAll);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<HorarioDoctor>> getHorarioDoctorWithFilter(@Valid @RequestBody HorarioDoctorFilterDto filterDto) {
        try {
            List<HorarioDoctor> especialidades = horarioDoctorService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(especialidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioDoctorResponseDto> getHorarioDoctorById(@PathVariable Integer id) {
        try {
            HorarioDoctorResponseDto especialidad = horarioDoctorService.getById(id);
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
    public ResponseEntity<HorarioDoctorResponseDto> createHorarioDoctor(@Valid @RequestBody HorarioDoctorInsertDto horarioDoctorInsertDto) {
        try {
            HorarioDoctorResponseDto createdEspecialidad = horarioDoctorService.save(horarioDoctorInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEspecialidad);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HorarioDoctorResponseDto> updateHorarioDoctor(
            @PathVariable Integer id,
            @Valid @RequestBody HorarioDoctorUpdateDto especialidadUpdateDto) {
        try {
            HorarioDoctorResponseDto updatedEspecialidad = horarioDoctorService.update(id, especialidadUpdateDto);
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
    public ResponseEntity<Void> deleteHorarioDoctor(@PathVariable Integer id) {
        try {
            Boolean deleted = horarioDoctorService.delete(id);
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