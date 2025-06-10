package com.habimed.habimedWebService.resenia.application;

import com.habimed.habimedWebService.resenia.domain.service.ReseniaService;
import com.habimed.parameterREST.PeticionREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultorio")
public class ReseniaController extends PeticionREST {
    private ReseniaService reseniaService;

    @Autowired
    public ReseniaController(ReseniaService reseniaService) {
        this.reseniaService = reseniaService;
    }

    /*
     getListResenias por consultorio
     */


    /*
     setResenia solo por pacientes a un consultorio con el que haya tenido una cita en los ultimos 7 dias
     */


    /*
     deleteResenia solo por el usuario que la registró y el usuario consultor
     */

}
