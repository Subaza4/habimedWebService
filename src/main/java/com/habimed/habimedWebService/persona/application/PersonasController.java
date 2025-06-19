package com.habimed.habimedWebService.persona.application;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.service.PersonaService;
import com.habimed.habimedWebService.persona.dto.PersonaFilterDto;
import com.habimed.habimedWebService.persona.dto.PersonaInsertDto;
import com.habimed.habimedWebService.persona.dto.PersonaUpdateDto;
import com.habimed.parameterREST.ApiResponse;
import com.habimed.parameterREST.PeticionREST; // Mantén esta si tu clase base la usa
import com.habimed.parameterREST.ResponseREST; // Importa tu clase ResponseREST

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Para devolver la respuesta HTTP completa
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList; // Para listas vacías
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persona")
public class PersonasController extends PeticionREST { // Si PeticionREST no es estrictamente necesario o no aporta al JSON, considera quitarlo.

    private final PersonaService personaService;

    @Autowired
    public PersonasController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("getPersonas")
    public ResponseEntity<ApiResponse<List<Persona>>> findAllPersonas(@RequestBody PersonaFilterDto request) {
        try {
            List<Persona> personas = personaService.findAll(request);
            if (personas.isEmpty()) {
                return ResponseEntity.ok(
                        ApiResponse.success(new ArrayList<>(), "No se encontraron personas")
                );
            }
            return ResponseEntity.ok(
                    ApiResponse.success(personas, "Personas encontradas exitosamente")
            );

        } catch (Exception e) {
            System.out.println("Ocurrió un error al obtener las personas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Ocurrió un error al obtener las personas"));
        }
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ApiResponse<Persona>> findById(@PathVariable Long dni) {
        ApiResponse response = new ApiResponse();
        if (dni == null || dni < 10000000) {
            response.error("El DNI debe tener un valor mayor a 10000000");
            return ResponseEntity.badRequest().body(response);
        }else{
            try{
                Optional<Persona> persona = personaService.findById(dni);
                if (persona.isPresent()) {
                    return ResponseEntity.ok(ApiResponse.success(persona.get(), "Persona encontrada exitosamente"));
                } else {
                    return ResponseEntity.ok(ApiResponse.success(null, "No se encontró a la persona"));
                }
            }catch (Exception e) {
                System.out.println("Ocurrió un error al obtener las personas: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Ocurrió un error al obtener la persona"));
            }
        }
    }

    @PostMapping("/savePersona")
    public ResponseEntity<ApiResponse<Persona>> savePersona(@RequestBody PersonaInsertDto request) {
        if (request == null || request.getDni() == null)
            return ResponseEntity.ok(ApiResponse.error("El DNI no puede ser nulo"));

        try{
            Persona _persona = personaService.savePersona(request);
            if(_persona != null) {
                return ResponseEntity.ok(ApiResponse.success(_persona,"Persona registrada correctamente"));
            }else{
                return ResponseEntity.ok(ApiResponse.success(null, "No se pudo registrar la persona"));
            }
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Ocurrió un error al guardar a la persona"));
        }
    }

    @PatchMapping("/savePersona/{dni}")
    public ResponseEntity<ApiResponse<Persona>> updatePersona(@RequestBody PersonaUpdateDto request, @PathVariable Long dni) {
        if (request == null || dni == null || dni < 10000000) {
            return new ResponseEntity<>(ApiResponse.error("El DNI debe tener un valor mayor a 10000000"), HttpStatus.BAD_REQUEST);
        }

        try{
            //Update
            Persona _persona = personaService.updatePersona(request, dni);
            if(_persona != null) {
                return ResponseEntity.ok(ApiResponse.success(_persona, "Persona registrada correctamente"));
            }else{
                return ResponseEntity.ok(ApiResponse.success(null, "No se pudo registrar la persona"));
            }
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Ocurrió un error al guardar a la persona"));
        }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<ApiResponse> deletePersona(@PathVariable Long dni){
        ApiResponse response = new ApiResponse();
        try {
            if(dni == null){
                response.error("El DNI no puede ser nulo.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        
            if(personaService.deletePersona(dni)) {
                response.success(true,"Persona eliminada exitosamente.");
            } else {
                response.success(false,"No se encontró la persona a eliminar.");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        
        } catch (Exception e) {
            e.printStackTrace();
            response.error("Ocurrió un error al eliminar la persona.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}