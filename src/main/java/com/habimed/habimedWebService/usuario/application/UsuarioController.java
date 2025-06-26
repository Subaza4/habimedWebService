package com.habimed.habimedWebService.usuario.application;

import java.util.ArrayList;
import java.util.List;

import com.habimed.habimedWebService.resenia.repository.ReseniaRepository;
import com.habimed.habimedWebService.usuario.dto.*;
import com.habimed.parameterREST.ApiResponse;
import com.habimed.parameterREST.ResponseREST;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.parameterREST.PeticionREST;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends PeticionREST{

    private final ReseniaRepository reseniaRepository;
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, ReseniaRepository reseniaRepository){
        this.usuarioService = usuarioService;
        this.reseniaRepository = reseniaRepository;
    }

    @GetMapping("/getUsuarios")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> getListUsuarios(@RequestBody UsuarioFilterDto request) {
        ApiResponse response = new ApiResponse();
        try{
            List<UsuarioResponseDto> usuarios = this.usuarioService.findAllUsuarios(request);
            if (usuarios.isEmpty()) {
                response.success(new ArrayList<>(),"No se encontraron usuarios");
            } else {
                response.success(usuarios, "Usuarios encontrados exitosamente.");
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            response.setMessage("Error interno:"+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUsuario/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> getUsuario(@PathVariable Integer id){
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>();
        if(id == null || id <= 0){
            response.error("El DNI es obligatorio y debe ser un número válido.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try{
            UsuarioResponseDto usuario = this.usuarioService.findByIdUsuario(id);
            if(usuario == null){
                response.success(usuario);
                response.setMessage("Usuario encontrado");
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            response.setMessage("Error interno:"+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<ApiResponse> saveUsuario(@RequestBody UsuarioInsertDto request) {
        ResponseREST response = new ResponseREST();
        try{
            UsuarioResponseDto respuesta = this.usuarioService.saveUsuario(request);

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

        }catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error: "+e.getMessage()));
        }
    }

    @DeleteMapping("/{idusuario}")
    public ResponseEntity<ResponseREST> deleteUsuario(@PathVariable Integer idusuario) {
        ResponseREST response = new ResponseREST();
        boolean respuesta = this.usuarioService.deleteUsuario(idusuario);
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

    ////////////////////////////////////////////////////////////////////////////////////////
    /// Método para registrar un usuario nuevo
    @PostMapping("/registreUsuario")
    public ResponseEntity<ResponseREST> registreUsuario(@RequestBody UsuarioRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            Integer respuesta = this.usuarioService.setUsuario(request);
            if (respuesta == 1) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Usuario registrado exitosamente");
            } else if (respuesta == 2) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Ya existe una cuenta con ese usuario");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error al crear o actualizar el usuario");
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al registrar el usuario");
            response.setSalida(e.getMessage());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }
}
