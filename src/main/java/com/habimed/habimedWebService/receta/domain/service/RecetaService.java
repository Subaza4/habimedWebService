package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.receta.domain.model.Receta;

import java.util.List;

public interface RecetaService {

    public Receta getRecetaById(Long idReceta);
    public List<Receta> getAllRecetas();
    public Receta saveReceta(Receta receta);
    public Receta updateReceta(Receta receta);
    public void deleteReceta(Long idReceta);
}
