package com.habimed.habimedWebService.usuario.application;

import java.util.List;

import com.habimed.parameterREST.ResponseREST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.dto.UsuarioRequest;
import com.habimed.parameterREST.PeticionREST;

@RestController
@RequestMapping("/seguridad")
public class UsuarioController extends PeticionREST{
    
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping("/getUsuarios")
    public ResponseEntity<ResponseREST> getListUsuarios(@RequestBody UsuarioRequest request) {
        ResponseREST response = new ResponseREST();
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
        ResponseREST response = new ResponseREST();
        Integer respuesta =  this.usuarioService.setUsuario(request);
        if (respuesta == 1) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("usuario creado exitosamente.");
            return true;
        } else if (respuesta == 2) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("usuario actualizado exitosamente.");
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
