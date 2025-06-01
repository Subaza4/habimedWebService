package com.habimed.habimedWebService.tipoUsuario.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.parameter.personaRequest.PersonaRequest;
import com.habimed.habimedWebService.tipoUsuario.domain.model.TipoUsuario;
import com.habimed.habimedWebService.tipoUsuario.domain.service.TipoUsuarioService;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

@RestController
@RequestMapping("/seguridad")
public class TipoUsuarioController extends PeticionREST {
    
    private final TipoUsuarioService tipoUsuarioService;

    @Autowired
    public TipoUsuarioController(TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @PostMapping("/getTipoUsuario")
    public ResponseEntity<ResponsePRES> getTipoUsuario() {
        ResponsePRES response = new ResponsePRES();
        List<TipoUsuario> listTipoUsuario = tipoUsuarioService.getAllTipoUsuarioList();

        if (listTipoUsuario.isEmpty()) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontraron personas");
            response.setSalida(new ArrayList<>());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // O HttpStatus.OK, según tu preferencia
        } else {
            response.setStatus(STATUS_OK);
            response.setSalida(listTipoUsuario);
            response.setSalidaMsg("Personas encontradas exitosamente."); // Opcional
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
