package com.habimed.habimedWebService.detallePago.domain.service;

import lombok.RequiredArgsConstructor;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.detallePago.dto.*;
import com.habimed.habimedWebService.detallePago.repository.DetallePagoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetallePagoServiceImpl implements DetallePagoService {

    private final DetallePagoRepository detallePagoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DetallePago> findAll() {
        List<DetallePago> detallesPago = detallePagoRepository.findAll();
        return detallesPago.stream()
                .map(detallePago -> modelMapper.map(detallePago, DetallePago.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DetallePago> findAllWithConditions(DetallePagoFilterDto detallePagoFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con una de las opciones anteriores):
        List<DetallePago> detallesPago = detallePagoRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (detallePagoFilterDto.getIdDetallePago() != null) {
            detallesPago = detallesPago.stream()
                    .filter(dp -> dp.getIdDetallePago().equals(detallePagoFilterDto.getIdDetallePago()))
                    .collect(Collectors.toList());
        }
        
        if (detallePagoFilterDto.getMonto() != null) {
            detallesPago = detallesPago.stream()
                    .filter(dp -> dp.getMonto().compareTo(detallePagoFilterDto.getMonto()) >= 0)
                    .collect(Collectors.toList());
        }
        
        if (detallePagoFilterDto.getMetodoPago() != null) {
            detallesPago = detallesPago.stream()
                    .filter(dp -> dp.getMetodoPago().equals(detallePagoFilterDto.getMetodoPago()))
                    .collect(Collectors.toList());
        }
        
        if (detallePagoFilterDto.getEstadoPago() != null) {
            detallesPago = detallesPago.stream()
                    .filter(dp -> dp.getEstadoPago().equals(detallePagoFilterDto.getEstadoPago()))
                    .collect(Collectors.toList());
        }
        
        if (detallePagoFilterDto.getFechaPago() != null) {
            detallesPago = detallesPago.stream()
                    .filter(dp -> dp.getFechaPago().toLocalDate().equals(detallePagoFilterDto.getFechaPago().toLocalDate()))
                    .collect(Collectors.toList());
        }
        
        return detallesPago.stream()
                .map(detallePago -> modelMapper.map(detallePago, DetallePago.class))
                .collect(Collectors.toList());
    }

    @Override
    public DetallePagoResponseDto getById(Integer id) {
        Optional<DetallePago> detallePago = detallePagoRepository.findById(id);
        if (detallePago.isPresent()) {
            return modelMapper.map(detallePago.get(), DetallePagoResponseDto.class);
        }
        throw new RuntimeException("DetallePago no encontrado con ID: " + id);
    }

    @Override
    public DetallePagoResponseDto save(DetallePagoInsertDto detallePagoInsertDto) {
        DetallePago detallePago = modelMapper.map(detallePagoInsertDto, DetallePago.class);
        DetallePago savedDetallePago = detallePagoRepository.save(detallePago);
        return modelMapper.map(savedDetallePago, DetallePagoResponseDto.class);
    }

    @Override
    public DetallePagoResponseDto update(Integer id, DetallePagoUpdateDto detallePagoUpdateDto) {
        Optional<DetallePago> existingDetallePago = detallePagoRepository.findById(id);
        
        if (existingDetallePago.isPresent()) {
            DetallePago detallePago = existingDetallePago.get();
            
            // Actualizar solo los campos que no son null en el DTO
            if (detallePagoUpdateDto.getMonto() != null) {
                detallePago.setMonto(detallePagoUpdateDto.getMonto());
            }
            if (detallePagoUpdateDto.getMetodoPago() != null) {
                detallePago.setMetodoPago(detallePagoUpdateDto.getMetodoPago());
            }
            if (detallePagoUpdateDto.getEstadoPago() != null) {
                detallePago.setEstadoPago(detallePagoUpdateDto.getEstadoPago());
            }
            if (detallePagoUpdateDto.getFechaPago() != null) {
                detallePago.setFechaPago(detallePagoUpdateDto.getFechaPago());
            }
            if (detallePagoUpdateDto.getCita() != null) {
                detallePago.setCita(detallePagoUpdateDto.getCita());
            }
            
            DetallePago updatedDetallePago = detallePagoRepository.save(detallePago);
            return modelMapper.map(updatedDetallePago, DetallePagoResponseDto.class);
        }
        
        throw new RuntimeException("DetallePago no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        if (detallePagoRepository.existsById(id)) {
            detallePagoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}