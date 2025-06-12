package com.habimed.habimedWebService.persona.application;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.service.PersonaService;
import com.habimed.habimedWebService.persona.dto.PersonaRequest;
import com.habimed.parameterREST.PeticionREST; // Mantén esta si tu clase base la usa
import com.habimed.parameterREST.ResponseREST; // Importa tu clase ResponseREST

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
    public ResponseEntity<ResponseREST> getPersonas(@RequestBody PersonaRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<Persona> personas = personaService.getAllPersonas(request);
            if (personas.isEmpty()) {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontraron personas");
                response.setSalida(new ArrayList<>());
            } else {
                response.setStatus(STATUS_OK);
                response.setSalida(personas);
                response.setSalidaMsg("Personas encontradas exitosamente."); // Opcional
            }
            return ResponseEntity.ok(response);
        }catch(Exception e){
            response.setStatus(STATUS_KO);
            System.out.println("Ocurrió un error al obtener las personas: " + e.getMessage());
            response.setSalidaMsg("Ocurrió un error al obtener las personas");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/getPersona")
    public ResponseEntity<ResponseREST> getPersona(@RequestBody PersonaRequest request) {
        ResponseREST response = new ResponseREST();
        if (request == null || request.getDni() == null) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("DNI no proporcionado o inválido.");
            response.setSalida(null); // O un objeto vacío si prefieres, ej: new Object()
            return ResponseEntity.ok(response);
        }else{
            try{
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
            }catch (Exception e) {
                response.setStatus(STATUS_KO);
                System.out.println("Ocurrió un error al obtener los datos de la persona: " + e.getMessage());
                response.setSalidaMsg("Ocurrió un error al obtener los datos de la persona");
                return ResponseEntity.ok(response);
            }
        }
    }

    @PostMapping("/setPersona")
    public ResponseEntity<ResponseREST> setPersona(@RequestBody PersonaRequest request) {
        ResponseREST response = new ResponseREST();
        
        if (request == null || request.getDni() == null) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("El parámetro persona no puede ser nulo o el DNI no puede ser nulo.");
            response.setSalida(null);
            return ResponseEntity.ok(response);
        }

        try{
            int resultado = personaService.setPersona(request);
            Map<String, Object> salidaMap = new HashMap<>();
            salidaMap.put("resultado", resultado);

            switch (resultado) {
                case 0:
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("Error: DNI no proporcionado.");
                    response.setSalida(salidaMap);
                    break;
                case 1:
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("Persona registrada exitosamente.");
                    response.setSalida(salidaMap);
                    break;
                case 2:
                    response.setStatus(STATUS_OK);
                    response.setSalidaMsg("Persona actualizada exitosamente.");
                    response.setSalida(salidaMap);
                    break;
                default:
                    response.setStatus(STATUS_KO);
                    response.setSalidaMsg("Error desconocido al procesar la solicitud.");
                    response.setSalida(salidaMap);
            }

            return ResponseEntity.ok(response);
        }catch (Exception e) {
            System.out.println("[setPersona] Error en la peticion: ");
            e.printStackTrace();
            response.setStatus(STATUS_KO);
            response.setSalidaMsg(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/deletePersona")
    public ResponseEntity<ResponseREST> deletePersona(@RequestBody PersonaRequest request){
        ResponseREST response = new ResponseREST();
        try {
            if(request.getDni() == null){
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("El DNI no puede ser nulo.");
                return ResponseEntity.ok(response);
            }
        
            Boolean eliminado = personaService.deletePersona(request.getDni());
            if(eliminado) {
                response.setStatus(STATUS_OK);
                response.setSalidaMsg("Persona eliminada exitosamente.");
            } else {
                response.setStatus(STATUS_KO);
                response.setSalidaMsg("No se encontró la persona a eliminar.");
            }
            return ResponseEntity.ok(response);
        
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Ocurrió un error al eliminar la persona.");
            return ResponseEntity.ok(response);
        }
    }
}