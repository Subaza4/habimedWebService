package com.habimed.habimedWebService.permisoHistorial.domain.service;

import com.habimed.habimedWebService.permisoHistorial.domain.model.PermisosHistorial;
import com.habimed.habimedWebService.permisoHistorial.dto.*;

import java.util.List;

public interface PermisoHistorialService {
    List<PermisosHistorial> findAll();
    List<PermisosHistorial> findAllWithConditions(PermisoHistorialFilterDto permisoHistorialFilterDto);
    PermisoHistorialResponseDto getById(Integer id);
    PermisoHistorialResponseDto save(PermisoHistorialnsertDto permisoHistorialnsertDto);
    Boolean delete(Integer id);
    PermisoHistorialResponseDto update(Integer id, PermisoHistoriaUpdateDto permisoHistoriaUpdateDto);
}
