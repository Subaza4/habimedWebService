package com.habimed.habimedWebService.diagnostico.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.diagnostico.domain.service.DiagnosticoService;
import com.habimed.parameterREST.PeticionREST;
import com.habimed.parameterREST.ResponsePRES;

@RestController
@RequestMapping("/citas")
public class DiagnosticoControler extends PeticionREST {

    private static DiagnosticoService diagnosticoService;
    
    @Autowired
    public DiagnosticoControler(DiagnosticoService diagnosticoService) {
        DiagnosticoControler.diagnosticoService = diagnosticoService;
    }

    @PostMapping("/getDiagnosticos")
    public ResponseEntity<ResponsePRES> getDiagnosticos() {
        ResponsePRES response = new ResponsePRES();
        
        try {
            response.setData(diagnosticoService.getAllDiagnosticos(getRequest()));
            response.setStatus("success");
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setMessage("Error al obtener los diagnósticos: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    
}
