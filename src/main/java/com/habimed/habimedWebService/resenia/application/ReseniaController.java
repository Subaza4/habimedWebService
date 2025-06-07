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


}
