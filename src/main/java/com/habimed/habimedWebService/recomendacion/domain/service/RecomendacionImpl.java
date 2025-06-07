package com.habimed.habimedWebService.recomendacion.domain.service;

import com.habimed.habimedWebService.recomendacion.repository.RecomendacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecomendacionImpl implements RecomendacionService{

    private final RecomendacionRepository recomendacionRepository;

    @Autowired
    public RecomendacionImpl(RecomendacionRepository recomendacionRepository) {
        this.recomendacionRepository = recomendacionRepository;
    }


}
