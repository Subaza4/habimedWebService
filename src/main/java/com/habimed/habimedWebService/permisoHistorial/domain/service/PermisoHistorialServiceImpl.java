package com.habimed.habimedWebService.permisoHistorial.domain.service;

import com.habimed.habimedWebService.permisoHistorial.repository.PermisoHistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermisoHistorialServiceImpl implements PermisoHistorialService{
    private PermisoHistorialRepository permisoHistorialRepository;

    @Autowired
    public PermisoHistorialServiceImpl(PermisoHistorialRepository permisoHistorialRepository) {
        this.permisoHistorialRepository = permisoHistorialRepository;
    }


}
