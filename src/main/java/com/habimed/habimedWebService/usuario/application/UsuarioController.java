package com.habimed.habimedWebService.usuario.application;

import java.util.List;

import com.habimed.habimedWebService.resenia.repository.ReseniaRepository;
import com.habimed.habimedWebService.usuario.dto.LoginRequest;
import com.habimed.parameterREST.ResponseREST;
import org.apache.coyote.Response;
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

    private final ReseniaRepository reseniaRepository;
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, ReseniaRepository reseniaRepository){
        this.usuarioService = usuarioService;
        this.reseniaRepository = reseniaRepository;
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
    @PostMapping("/getUsuario")
    public ResponseEntity<ResponseREST> getUsuario(@RequestBody UsuarioRequest request){
        ResponseREST response = new ResponseREST();
        if(request.getDniPersona().describeConstable().isEmpty()){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("El DNI es obligatorio");
        }else{
            UsuarioDTO usuario = this.usuarioService.getUsuario(request.getDniPersona());
            if(usuario == null){
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontró el usuario con DNI: " + request.getDniPersona());
            }else{
                response.setStatus(STATUS_OK);
                response.setSalida(usuario);
                response.setSalidaMsg("Usuario encontrado");
            }
        }
        return ResponseEntity.ok(response);
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

    @PostMapping("/login")
    public ResponseEntity<ResponseREST> login(@RequestBody LoginRequest request){
        ResponseREST response = new ResponseREST();
        if(request.getUsuario() == null || request.getContrasenia() == null ||
            request.getUsuario().isEmpty() || request.getContrasenia().isEmpty()){
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("El usuario y/o contraseña no pueden estar en blanco");
        }else {
            UsuarioDTO usuario = this.usuarioService.loginUser(request);
            if (usuario == null) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Usuario no encontrado");
            } else {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Usuario encontrado");
                response.setSalida(usuario);
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseREST> logout(@RequestBody LoginRequest request){
        ResponseREST response = new ResponseREST();
        try{
            if(request.getToken() == null || request.getToken().isEmpty()){
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Necesario el token para desloguear el usuario");
            }else {
                if(this.usuarioService.logoutUser(request.getToken())){
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("Usuario deslogueado exitosamente");
                }else{
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("No se pudo desloguear al usuario");
                }
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al desloguear el usuario");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
