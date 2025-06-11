package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.receta.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecetaServiceImpl implements RecetaService {
    private final RecetaRepository recetaRepository;

    @Autowired
    public RecetaServiceImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    public Receta getRecetaById(Long idReceta){

    }
    public List<Receta> getAllRecetas();
    public Receta saveReceta(Receta receta);
    public Receta updateReceta(Receta receta);
    public void deleteReceta(Long idReceta);
}
