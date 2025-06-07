package com.habimed.habimedWebService.permisoHistorial.application;

import com.habimed.habimedWebService.permisoHistorial.domain.service.PermisoHistorialService;
import com.habimed.parameterREST.PeticionREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seguridad")
public class PermisoHistorialController extends PeticionREST {
    private final PermisoHistorialService permisoHistorialService;

    @Autowired
    public PermisoHistorialController(PermisoHistorialService permisoHistorialService) {
        this.permisoHistorialService = permisoHistorialService;
    }

}
