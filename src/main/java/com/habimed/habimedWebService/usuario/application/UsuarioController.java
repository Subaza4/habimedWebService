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

    /*
     1. **Códigos de retorno**:
        - 1: Inserción exitosa
        - 2: Actualización exitosa
        - 3: Usuario no existe
        - 4: Parámetros nulos
        - 5: DNI nulo
        - 6: DNI no existe
        - 7: Tipo usuario no existe
     */
    @PostMapping("/setUsuario")
    public ResponseEntity<ResponseREST> setUsuario(@RequestBody UsuarioRequest request) {
        ResponseREST response = new ResponseREST();
        Integer respuesta = this.usuarioService.setUsuario(request);
        if (respuesta == 1) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Inserción exitosa");
        } else if (respuesta == 2) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Actualización exitosa");
        } else if (respuesta == 3) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Usuario no existe");
        } else if (respuesta == 4) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Parámetros nulos");
        } else if (respuesta == 5) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("DNI nulo");
        } else if (respuesta == 6) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("DNI no existe");
        } else if (respuesta == 7) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Tipo usuario no existe");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al crear o actualizar el usuario");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteUsuario")
    public ResponseEntity<ResponseREST> deleteUsuario(@RequestBody String dni) {
        ResponseREST response = new ResponseREST();
        boolean respuesta = this.usuarioService.deleteUsuario(dni);
        if (respuesta) {
            response.setStatus(STATUS_OK);
            response.setSalidaMsg("Usuario eliminado exitosamente");
        } else {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Ocurrió un error al eliminar el usuario");
        }
        return ResponseEntity.ok(response);
    }
    
}
