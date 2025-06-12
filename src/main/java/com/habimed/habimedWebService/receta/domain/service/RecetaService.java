package com.habimed.habimedWebService.receta.domain.service;

import com.habimed.habimedWebService.receta.dto.RecetaDTO;
import com.habimed.habimedWebService.receta.dto.RecetaRequest;

import java.util.List;

public interface RecetaService {

    List<RecetaDTO> getRecetas(RecetaRequest request);

    RecetaDTO getRecetaById(RecetaRequest request);

    Integer setReceta(RecetaRequest request);

    Boolean deleteReceta(RecetaRequest request);

}
