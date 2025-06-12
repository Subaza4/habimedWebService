package com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.model.DoctorTrabajaConsultorio;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.repository.DoctorTrabajaConsultorioRepository;

@Service
@RequiredArgsConstructor
public class DoctorTrabajaConsultorioServiceImpl implements DoctorTrabajaConsultorioService {

    private final DoctorTrabajaConsultorioRepository doctorTrabajaConsultorioRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public List<DoctorTrabajaConsultorioDTO> getAllDoctorsTrabajaConsultorioWithDetails(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.getAllDoctorsTrabajaConsultorio(request);
    }

    @Override
    public List<DoctorTrabajaConsultorioResponseDto> findAllDoctorsTrabajaConsultorio(){
        List<DoctorTrabajaConsultorio> d = doctorTrabajaConsultorioRepository.findAll();
        return d.stream().map(x -> modelMapper.map(x, DoctorTrabajaConsultorioResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public DoctorTrabajaConsultorioResponseDto addDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        DoctorTrabajaConsultorio d = doctorTrabajaConsultorioRepository.setDoctorTrabajaConsultorio(request);
        return modelMapper.map(d, DoctorTrabajaConsultorioResponseDto.class);
    }

    @Override
    public List<DoctorTrabajaConsultorioResponseDto> getDoctorConsultorioByIdConsultorio(Integer idConsultorio){
        List<DoctorTrabajaConsultorio> d = doctorTrabajaConsultorioRepository.findByIdConsultorio();
        return d.stream().map(x -> modelMapper.map(x, DoctorTrabajaConsultorioResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<DoctorTrabajaConsultorioResponseDto> getDoctorConsultorioByIdDoctor(Integer idDoctor){
        List<DoctorTrabajaConsultorio> d = doctorTrabajaConsultorioRepository.findByIdServicio();
        return d.stream().map(x -> modelMapper.map(x, DoctorTrabajaConsultorioResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        return doctorTrabajaConsultorioRepository.deleteDoctorTrabajaConsultorio(request);
    }
    

}
