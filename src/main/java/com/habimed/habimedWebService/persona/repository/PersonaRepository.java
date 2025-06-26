package com.habimed.habimedWebService.persona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.habimed.habimedWebService.persona.domain.model.Persona;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {  // Cambiado Integer por Long para el DNI

//    @Query("SELECT p FROM Persona p WHERE " +
//           "(:dni IS NULL OR p.dni = :dni) AND " +
//           "(:nombres IS NULL OR LOWER(p.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))) AND " +
//           "(:apellidos IS NULL OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :apellidos, '%'))) AND " +
//           //"(:correo IS NULL OR LOWER(p.correo) LIKE LOWER(CONCAT('%', :correo, '%'))) AND " +
//           "(:fechaNacimiento IS NULL OR p.fechaNacimiento = :fechaNacimiento)")
//    List<Persona> findWithFilters(@Param("dni") Long dni,
//                                  @Param("nombres") String nombres,
//                                  @Param("apellidos") String apellidos,
//                                  //@Param("correo") String correo,
//                                  @Param("fechaNacimiento") LocalDate fechaNacimiento);
//
//    // Métodos adicionales útiles para Persona
//    List<Persona> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
//
//    boolean existsByCorreo(String correo);
//
//    List<Persona> findByFechaNacimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);
}