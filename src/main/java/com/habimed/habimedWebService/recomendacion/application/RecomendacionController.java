package com.habimed.habimedWebService.recomendacion.application;

import com.habimed.habimedWebService.recomendacion.domain.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultorio")
public class RecomendacionController {
    private RecomendacionService recomendacionService;

    @Autowired
    public RecomendacionController(RecomendacionService recomendacionService) {
        this.recomendacionService = recomendacionService;
    }

    /*
      getRecomendaciones por id de la cita tanto para pacientes como doctores
     */



    /*
      setRecomendaciones solo por los doctores
     */



    /*
      No se pueden eliminar las recomendaciones
     */

}
