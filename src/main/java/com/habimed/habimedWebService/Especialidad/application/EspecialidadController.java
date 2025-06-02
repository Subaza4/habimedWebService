package com.habimed.habimedWebService.especialidad.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.especialidad.domain.parameter.tipoUsuarioRequest.TipoUsuarioRequest;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadService;
import com.habimed.habimedWebService.especialidad.domain.service.EspecialidadServiceImpl;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/especialidad")
public class EspecialidadController extends PeticionREST{

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadServiceImpl especialidadService) {
        this.especialidadService = especialidadService;
    }


    @PostMapping("/getEspecialidades")
    public ResponseEntity<ResponsePRES> getEspecialidades(@RequestBody @Valid TipoUsuarioRequest request) {
        return ResponseEntity.ok(new ResponsePRES());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponsePRES> createEspecialidad(@RequestBody @Valid TipoUsuarioRequest request) {
        return ResponseEntity.ok(new ResponsePRES());
    }

    @PutMapping("/update")
    public ResponseEntity<ResponsePRES> updateEspecialidad(@RequestBody @Valid TipoUsuarioRequest request) {
        // ... tu lógica para actualizar la especialidad
        return ResponseEntity.ok(new ResponsePRES());
    }
}
