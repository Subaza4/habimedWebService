package com.habimed.habimedWebService.persona.application;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.parameter.personaRequest.PersonaRequest;
import com.habimed.habimedWebService.persona.domain.service.PersonaService;
import com.habimed.parameterREST.PeticionREST; // Mantén esta si tu clase base la usa
import com.habimed.parameterREST.ResponsePRES; // Importa tu clase ResponsePRES

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Para devolver la respuesta HTTP completa
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList; // Para listas vacías
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/seguridad")
public class PersonasController extends PeticionREST { // Si PeticionREST no es estrictamente necesario o no aporta al JSON, considera quitarlo.

    private final PersonaService personaService;

    @Autowired
    public PersonasController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping("/getPersonas")
    public ResponseEntity<ResponsePRES> getPersonas(@RequestBody PersonaRequest request) {
        ResponsePRES response = new ResponsePRES();
        List<Persona> personas = personaService.getAllPersonas();

        if (personas.isEmpty()) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("No se encontraron personas");
            response.setSalida(new ArrayList<>());
            return ResponseEntity.ok(response);
        } else {
            response.setStatus(STATUS_OK);
            response.setSalida(personas);
            response.setSalidaMsg("Personas encontradas exitosamente."); // Opcional
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/getPersona")
    public ResponseEntity<ResponsePRES> getPersona(@RequestBody PersonaRequest request) {
        ResponsePRES response = new ResponsePRES();
        if (request == null || request.getDni() == null) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("DNI no proporcionado o inválido.");
            response.setSalida(null); // O un objeto vacío si prefieres, ej: new Object()
            return ResponseEntity.ok(response);
        }else{
            Optional<Persona> persona = personaService.getPersonaById(request.getDni()); // Asume que PersonaRequest tiene un método getDni()

            if (persona.isPresent()) {
                response.setStatus(STATUS_OK);
                response.setSalida(persona.get());
                response.setSalidaMsg("Persona encontrada exitosamente.");
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontró persona con DNI: " + request.getDni());
                response.setSalida(null); // O un objeto vacío si prefieres, ej: new Object()
                return ResponseEntity.ok(response);
            }
        }
    }

    @PostMapping("/setPersona")
    public ResponseEntity<ResponsePRES> setPersona(@RequestBody PersonaRequest request) {
        ResponsePRES response = new ResponsePRES();
         if (request == null || request.getDni() == null) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("El parametro persona no puede ser nulo o el DNI no puede ser nulo.");
            response.setSalida(null);
            return ResponseEntity.ok(response);
        }else{
            int rowsAffected = personaService.savePersona(request); 

            if (rowsAffected > 0) { // Si la operación fue exitosa
                 Map<String, Integer> salidaMap = new HashMap<>();
                salidaMap.put("count", rowsAffected);
                
                response.setSalida(salidaMap);
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Persona guardada/actualizada exitosamente.");
                return ResponseEntity.ok(response); // O HttpStatus.OK si es actualización
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("Error al guardar/actualizar persona.");
                response.setSalida(null);
                return ResponseEntity.ok(response); // O un error más específico
            }
        }
    }
}