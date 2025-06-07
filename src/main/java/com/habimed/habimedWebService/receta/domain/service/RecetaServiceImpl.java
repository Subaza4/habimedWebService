package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.receta.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecetaServiceImpl implements RecetaService {
    private final RecetaRepository recetaRepository;

    @Autowired
    public RecetaServiceImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }


}
