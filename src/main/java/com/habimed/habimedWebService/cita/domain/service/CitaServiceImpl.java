package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.dto.CitaResponseDto;
import com.habimed.habimedWebService.cita.dto.CitaUpdateDto;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.dto.UsuarioDTO;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.cita.dto.CitaRequest;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.cita.repository.CitaRepository;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService{

    private final UsuarioService usuarioService;
    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CitaDTO> getCitas(CitaRequest request) {
        return citaRepository.getCitas(request);
    }

    @Override
    public CitaDTO getCitaById(Integer id) {
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
                return citaRepository.deleteCita(cita.getIdcita(), usuario.getIdusuario());
            }
        }
        return false;
    }

    @Override
    public CitaResponseDto updateCita(Integer id, CitaUpdateDto cita) {
        Cita updated = new Cita();                          // falta implementar función en el repositorio para
                                                            // poder actualizar cualquier campo que venga en el DTO
        return modelMapper.map(updated, CitaResponseDto.class);
    }
}
