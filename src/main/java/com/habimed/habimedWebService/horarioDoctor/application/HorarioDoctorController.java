package com.habimed.habimedWebService.horarioDoctor.application;

import com.habimed.habimedWebService.horarioDoctor.domain.service.HorarioDoctorService;
import com.habimed.parameterREST.PeticionREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consultorio")
public class HorarioDoctorController extends PeticionREST {
    private final HorarioDoctorService horarioDoctorService;

    @Autowired
    public HorarioDoctorController(HorarioDoctorService horarioDoctorService) {
        this.horarioDoctorService = horarioDoctorService;
    }


}
