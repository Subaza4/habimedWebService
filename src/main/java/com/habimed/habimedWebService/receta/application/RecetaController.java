package com.habimed.habimedWebService.receta.application;

import com.habimed.habimedWebService.receta.domain.service.RecetaService;
import com.habimed.parameterREST.PeticionREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cita")
public class RecetaController extends PeticionREST {

    private RecetaService recetaService;

    @Autowired
    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    /*
      getReceta por id de cita
     */

    /*
      saveReceta solo por doctores y está asociado a una cita
     */


    /*
      No se puede eliminar ni editar las recetas
     */

}
