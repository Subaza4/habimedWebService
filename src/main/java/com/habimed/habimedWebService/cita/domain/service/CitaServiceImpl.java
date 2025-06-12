package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.cita.repository.CitaRepository;

@Service
public class CitaServiceImpl implements CitaService{

    private final UsuarioService usuarioService;
    private CitaRepository citaRepository;
    private UsuarioService UsuarioService;
    
    @Autowired
    public CitaServiceImpl(CitaRepository citaRepository, UsuarioService usuarioService) {
        this.citaRepository = citaRepository;
        this.usuarioService = usuarioService;
        this.UsuarioService = usuarioService;
    }

    @Override
    public List<CitaDTO> getCitas(CitaRequest request) {
        return citaRepository.getCitas(request);
    }

    @Override
    public CitaDTO getCita(Integer id) {
        return citaRepository.getCita(id);
    }

    @Override
    public Integer setCita(CitaRequest cita) {
        return citaRepository.setCita(cita);
    }

    @Override
    public Boolean deleteCita(CitaRequest cita) {
        if(cita.getIdcita() != null && cita.getIdcita() > 0){
            UsuarioDTO usuario = this.usuarioService.getUsuarioByToken(cita.getToken());
            if(usuario != null && usuario.getIdusuario() > 0){
                //return citaRepository.deleteCita(cita.getIdcita(), usuario.getIdusuario());
                return citaRepository.deleteCita(cita.getIdcita());
            }
        }
        return false;
    }
}
