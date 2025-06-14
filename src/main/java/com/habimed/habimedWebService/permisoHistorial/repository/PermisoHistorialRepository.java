package com.habimed.habimedWebService.permisoHistorial.repository;

import com.habimed.habimedWebService.permisoHistorial.domain.model.PermisosHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoHistorialRepository extends JpaRepository<PermisosHistorial, Integer> {

}
