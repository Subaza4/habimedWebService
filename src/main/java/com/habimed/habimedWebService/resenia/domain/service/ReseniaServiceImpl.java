package com.habimed.habimedWebService.resenia.domain.service;

import com.habimed.habimedWebService.resenia.repository.ReseniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReseniaServiceImpl implements ReseniaService{

    private final ReseniaRepository reseniaRepository;

    @Autowired
    public ReseniaServiceImpl(ReseniaRepository reseniaRepository) {
        this.reseniaRepository = reseniaRepository;
    }


}
