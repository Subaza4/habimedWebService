package com.habimed.habimedWebService.consultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;
import com.habimed.habimedWebService.consultorio.repository.ConsultorioRepository;

@Service
public class ConsultorioServiceImpl implements ConsultorioService {

    private ConsultorioRepository consultorioRepository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConsultorioServiceImpl(ConsultorioRepository consultorioRepository) {
        this.consultorioRepository = consultorioRepository;
    }

    @Override
    public List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request) {
        //Boolean usuarioLogueado = this.usuarioRepository.validateToken(request.getToken());
        //if(usuarioLogueado){
            //validar el tipo de usuario
            // Implementación para obtener todos los consultorios
            return consultorioRepository.getAllConsultorios(request);
        //}
        //return null;
    }

    @Override
    public ConsultorioDTO getConsultorioById(ConsultorioRequest request) {
        //Boolean usuarioLogueado = this.usuarioRepository.validateToken(request.getToken());
        //if(usuarioLogueado){
            // Implementación para obtener un consultorio por ID
            return consultorioRepository.getConsultorio(request.getIdconsultorio());
        //}
        //return null;
    }

    @Override
    public Integer setConsultorio(ConsultorioRequest request) {
        // Implementación para crear un nuevo consultorio
        return consultorioRepository.setConsultorio(request);
    }

    @Override
    public boolean deleteConsultorio(ConsultorioRequest request) {
        if( request.getIdconsultorio() == null ){
            return false;
        }else{
            return consultorioRepository.deleteConsultorio(request.getIdconsultorio());
        }
    }
    
}
