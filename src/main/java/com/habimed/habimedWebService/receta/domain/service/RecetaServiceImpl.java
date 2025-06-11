package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.receta.dto.RecetaDTO;
import com.habimed.habimedWebService.receta.dto.RecetaRequest;
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

    @Override
    public List<RecetaDTO> getRecetas(RecetaRequest request) {
        return recetaRepository.getRecetas(request);
    }

    @Override
    public RecetaDTO getRecetaById (RecetaRequest request){
        return  null;
    }

    @Override
    public Integer setReceta(RecetaRequest request) {
        return 0;
    }

    @Override
    public boolean deleteReceta(RecetaRequest request) {
        return false;
    }
}
