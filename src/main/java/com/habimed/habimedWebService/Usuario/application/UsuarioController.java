package com.habimed.habimedWebService.usuario.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.usuario.domain.parameter.request.UsuarioRequest;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.repository.dto.UsuarioDTO;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

@RestController
@RequestMapping("/seguridad")
public class UsuarioController extends PeticionREST{
    
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/getUsuarios")
    public ResponseEntity<ResponsePRES> getListUsuarios(@RequestBody UsuarioRequest request) {
        ResponsePRES response = new ResponsePRES();
        List<UsuarioDTO> usuarios = this.usuarioService.getListUsuarios(request);
        if (usuarios.isEmpty()) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontraron usuarios");
            response.setSalida(List.of());
            return ResponseEntity.ok(response);
        } else {
            response.setStatus(STATUS_OK);
            response.setSalida(usuarios);
            response.setSalidaMsg("Usuarios encontrados exitosamente.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/setUsuario")
    public boolean setUsuario(@RequestBody UsuarioRequest request) {
        ResponsePRES response = new ResponsePRES();
        Integer respuesta =  this.usuarioService.setUsuario(request);
        if (respuesta == 1) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Usuario creado exitosamente.");
            return true;
        } else if (respuesta == 2) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Usuario actualizado exitosamente.");
            return true;
        } else if(respuesta == 0){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Tipo de usuario no existe.");
            return false;
        }else{
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al crear o actualizar el usuario.");
            return false;
        }
    }
    
}
