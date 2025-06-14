package com.habimed.habimedWebService.permisoHistorial.repository;

import com.habimed.habimedWebService.permisoHistorial.domain.model.PermisosHistorial;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PermisoHistorialRepository extends JpaRepository<PermisosHistorial, Integer> {

}
