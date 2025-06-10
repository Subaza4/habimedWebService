package com.habimed.habimedWebService.diagnostico.application;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;
import com.habimed.parameterREST.ResponseREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.diagnostico.domain.service.DiagnosticoService;
import com.habimed.parameterREST.PeticionREST;

import java.util.List;

@RestController
@RequestMapping("/citas")
public class DiagnosticoController extends PeticionREST {

    private final DiagnosticoService diagnosticoService;

    @Autowired
    public DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    /*
      getDiagnostico solo por id de la cita y el id del doctor
     */
    @PostMapping("/getDiagnosticos")
    public ResponseEntity<ResponseREST> getDiagnosticos(DiagnosticoRequest request) {
        ResponseREST response = new ResponseREST();
        List<DiagnosticoDTO> result = diagnosticoService.getAllDiagnosticos(request);
        try {
            response.setSalida(result);
            response.setStatus("success");
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener los diagnósticos: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    /*
      SetDiagnostico solo puede realizar el usuario del doctor que atiende a la persona que reservó su cita
     */


    /*
      No se puede eliminar un diagnostico
     */
}
