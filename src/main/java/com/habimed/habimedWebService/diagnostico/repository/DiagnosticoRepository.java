package com.habimed.habimedWebService.diagnostico.repository;

import java.time.LocalDate;
import java.util.List;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoDTO;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoRequest;

@Repository
public class DiagnosticoRepository {
    
    private JdbcTemplate jdbcTemplate;
    private DiagnosticoDTO dto = new DiagnosticoDTO();

    @Autowired
    public DiagnosticoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Diagnostico> findAllDiagnosticos() {
        String sql = "SELECT iddiagnostico, idcita, descripcion, fecha_diagnostico " +
                "FROM medic.\"diagnostico\" " +
                "ORDER BY iddiagnostico DESC";

        return jdbcTemplate.query(sql, dto.getRowMapper());
    }


    public List<Diagnostico> findAllDiagnosticosByConditions(DiagnosticoRepositoryDTO request) {
        String sql = "SELECT iddiagnostico, idcita, descripcion, fecha_diagnostico " +
                "FROM medic.\"diagnostico\" " +
                request.getConditions() +
                " ORDER BY iddiagnostico DESC OFFSET ? LIMIT ?";

        Integer size = request.getTamanioPagina();
        Integer pagina = (request.getPagina() - 1) * size;

        return jdbcTemplate.query(sql, dto.getRowMapper(), pagina, size);
    }


    public Diagnostico getDiagnosticosById(Integer idDiagnostico) {
        if (idDiagnostico == null) {
            throw new IllegalArgumentException("El ID del diagnóstico no puede ser nulo");
        }

        String sql = "SELECT iddiagnostico, idcita, descripcion, fecha_diagnostico " +
                "FROM medic.\"diagnostico\" WHERE iddiagnostico = ?";

        try {
            return jdbcTemplate.queryForObject(sql, dto.getRowMapper(), idDiagnostico);
        } catch (EmptyResultDataAccessException e) {
            // No se encontró ningún diagnóstico con ese ID
            return null;
        } catch (DataAccessException e) {
            // Error en el acceso a la base de datos
            throw new RuntimeException("Error al consultar el diagnóstico con ID: " + idDiagnostico, e);
        }
    }


    public Diagnostico insertDiagnostico(DiagnosticoRepositoryDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("El diagnóstico no puede ser nulo");
        }

        if (request.getIdcita() == null) {
            throw new IllegalArgumentException("El ID de la cita es obligatorio");
        }

        if (request.getDescripcion() == null || request.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La descripción del diagnóstico es obligatoria");
        }

        String sql = "INSERT INTO medic.\"diagnostico\" (idcita, descripcion, fecha_diagnostico) " +
                "VALUES (?, ?, ?) RETURNING iddiagnostico";

        try {
            // Si no se proporciona la fecha, usar la fecha actual
            LocalDate fechaDiagnostico = request.getFecha_diagnostico();
            if (fechaDiagnostico == null) {
                fechaDiagnostico = LocalDate.now();
            }

            // Ejecutar la inserción y obtener el ID generado
            Integer idDiagnostico = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    request.getIdcita(),
                    request.getDescripcion(),
                    fechaDiagnostico
            );

            // Crear un nuevo objeto con el ID generado y los demás datos
            Diagnostico diagnosticoInsertado = new Diagnostico();
            diagnosticoInsertado.setIddiagnostico(idDiagnostico);
            diagnosticoInsertado.setIdcita(request.getIdcita());
            diagnosticoInsertado.setDescripcion(request.getDescripcion());
            diagnosticoInsertado.setFecha_diagnostico(fechaDiagnostico);

            return diagnosticoInsertado;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al insertar el diagnóstico: " + e.getMessage(), e);
        }
    }


    public Diagnostico updateDiagnostico(DiagnosticoRepositoryDTO request, Integer idDiagnostico) {
        if (request == null || idDiagnostico == null) {
            throw new IllegalArgumentException("El diagnóstico y su ID no pueden ser nulos");
        }

        String sql = "UPDATE medic.\"diagnostico\" SET " +
                "idcita = ?, descripcion = ?, fecha_diagnostico = ? " +
                "WHERE iddiagnostico = ?";

        try {
            // Si no se proporciona la fecha, mantener la existente
            LocalDate fechaDiagnostico = request.getFecha_diagnostico();
            if (fechaDiagnostico == null) {
                // Consultar la fecha actual en la base de datos
                String selectSql = "SELECT fecha_diagnostico FROM medic.\"diagnostico\" WHERE iddiagnostico = ?";
                java.sql.Date fecha = jdbcTemplate.queryForObject(
                        selectSql, java.sql.Date.class, idDiagnostico);
                if (fecha != null) {
                    fechaDiagnostico = fecha.toLocalDate();
                } else {
                    fechaDiagnostico = LocalDate.now();
                }
            }

            // Ejecutar la actualización
            int filasActualizadas = jdbcTemplate.update(
                    sql,
                    request.getIdcita(),
                    request.getDescripcion(),
                    fechaDiagnostico,
                    idDiagnostico
            );

            // Si se actualizó correctamente, devolver el objeto con todos sus datos
            if (filasActualizadas > 0) {
                Diagnostico diagnosticoActualizado = new Diagnostico();
                diagnosticoActualizado.setIddiagnostico(idDiagnostico);
                diagnosticoActualizado.setIdcita(request.getIdcita());
                diagnosticoActualizado.setDescripcion(request.getDescripcion());
                diagnosticoActualizado.setFecha_diagnostico(fechaDiagnostico);

                return diagnosticoActualizado;
            } else {
                // No se encontró el diagnóstico con ese ID
                return null;
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar el diagnóstico: " + e.getMessage(), e);
        }
    }



    public List<DiagnosticoDTO> getAllDiagnosticos(DiagnosticoRequest request) {
        // Implementación para obtener todos los diagnósticos
        return jdbcTemplate.query("SQL_QUERY_HERE", dto.getRowMapperDTO());
    }

    public DiagnosticoDTO getDiagnosticoById(Integer iddiagnostico) {
        // Implementación para obtener un diagnóstico por ID
        return jdbcTemplate.queryForObject("SQL_QUERY_HERE", new Object[]{iddiagnostico}, dto.getRowMapperDTO());
    }

    public Integer setDiagnostico(DiagnosticoRequest request) {
        // Implementación para insertar o actualizar un diagnóstico
        String sql = "INSERT INTO diagnostico (idcita, descripciondiagnostico, fechadiagnostico) VALUES (?, ?, ?) RETURNING iddiagnostico";
        return jdbcTemplate.queryForObject(sql, new Object[]{
            request.getIdcita(),
            request.getDescripcion(),
            request.getFecha_diagnostico()
        }, Integer.class);
    }

    public Boolean deleteDiagnostico(Integer iddiagnostico) {
        // Implementación para eliminar un diagnóstico
        String sql = "DELETE FROM diagnostico WHERE iddiagnostico = ?";
        int rowsAffected = jdbcTemplate.update(sql, iddiagnostico);
        return rowsAffected > 0;
    }
}
