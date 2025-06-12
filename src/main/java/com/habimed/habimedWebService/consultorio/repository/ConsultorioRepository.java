package com.habimed.habimedWebService.consultorio.repository;

import java.util.List;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRepositoryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;
import java.sql.CallableStatement;
import java.sql.Types;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;

@Repository
public class ConsultorioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final ConsultorioDTO dto = new ConsultorioDTO();

    @Autowired
    public ConsultorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Consultorio> findAllConsultorio() {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono " +
                "FROM medic.\"consultorio\" " +
                " ORDER BY idconsultorio DESC";

        return jdbcTemplate.query(sql, dto.consultorioRowMapper);
    }

    public List<Consultorio> findAllConsultorioByConditions(ConsultorioRepositoryDTO request) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono " +
                "FROM medic.\"consultorio\" " + request.getConditions() +
                " ORDER BY idconsultorio DESC OFFSET ? LIMIT ?";

        Integer size = request.getTamanioPagina();
        Integer pagina = (request.getPagina() -1 ) * size;
        return jdbcTemplate.query(sql, dto.consultorioRowMapper, pagina, size);
    }

    public Consultorio getConsultorioById(Integer idConsultorio) {
        if (idConsultorio == null) {
            throw new IllegalArgumentException("El ID del consultorio no puede ser nulo");
        }

        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono " +
                "FROM medic.\"consultorio\" WHERE idconsultorio = ?";

        try {
            // Consultamos por el consultorio
            Consultorio consultorio = jdbcTemplate.queryForObject(sql, dto.consultorioRowMapper, idConsultorio);

            Consultorio _consultorio = new Consultorio();
            _consultorio.setIdconsultorio(consultorio.getIdconsultorio());
            _consultorio.setNombre(consultorio.getNombre());
            _consultorio.setRuc(consultorio.getRuc());
            _consultorio.setUbicacion(consultorio.getUbicacion());
            _consultorio.setDireccion(consultorio.getDireccion());
            _consultorio.setTelefono(consultorio.getTelefono());

            return _consultorio;
        } catch (EmptyResultDataAccessException e) {
            // No se encontró ningún consultorio con ese ID
            return null;
        } catch (DataAccessException e) {
            // Error en el acceso a la base de datos
            throw new RuntimeException("Error al consultar el consultorio con ID: " + idConsultorio, e);
        }
    }

    /**
     * Inserta un nuevo consultorio en la base de datos
     * @param consultorio El objeto Consultorio con los datos a insertar
     * @return El consultorio insertado con su ID generado
     */
    public Consultorio insertConsultorio(ConsultorioRepositoryDTO consultorio) {
        if (consultorio == null) {
            throw new IllegalArgumentException("El consultorio no puede ser null");
        }
        Consultorio consultorioResponse = new Consultorio();
        String sql = "INSERT INTO medic.\"consultorio\" (ruc, nombre, ubicacion, direccion, telefono) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING idconsultorio";

        try {
            // Obtener el ID generado
            Integer idConsultorio = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    consultorio.getRuc(),
                    consultorio.getNombre(),
                    consultorio.getUbicacion(),
                    consultorio.getDireccion(),
                    consultorio.getTelefono()
            );

            if (idConsultorio == null) {
                throw new RuntimeException("No se pudo obtener el ID del consultorio insertado");
            }
            BeanUtils.copyProperties(consultorioResponse, consultorio);
            // Establecer el ID en el consultorio y devolverlo
            consultorioResponse.setIdconsultorio(idConsultorio);
            return consultorioResponse;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al insertar el consultorio", e);
        }
    }

    /**
     * Actualiza un consultorio existente en la base de datos
     * @param consultorio El objeto Consultorio con los datos actualizados
     * @return El consultorio actualizado o null si no se encontró
     */
    public Consultorio updateConsultorio(ConsultorioRepositoryDTO consultorio, Integer idConsultorio) {
        if (consultorio == null || idConsultorio == null) {
            throw new IllegalArgumentException("El consultorio y su ID no pueden ser null");
        }

        String sql = "UPDATE medic.\"consultorio\" " +
                "SET ruc = ?, nombre = ?, ubicacion = ?, direccion = ?, telefono = ? " +
                "WHERE idconsultorio = ?";

        try {
            int filasActualizadas = jdbcTemplate.update(
                    sql,
                    consultorio.getRuc(),
                    consultorio.getNombre(),
                    consultorio.getUbicacion(),
                    consultorio.getDireccion(),
                    consultorio.getTelefono(),
                    idConsultorio
            );
            Consultorio _consultorio = new Consultorio();
            _consultorio.setIdconsultorio(idConsultorio);
            BeanUtils.copyProperties(_consultorio, consultorio);
            // Si se actualizó correctamente, devolver el consultorio
            if (filasActualizadas > 0) {
                return _consultorio;
            } else {
                // No se encontró el consultorio con ese ID
                return null;
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar el consultorio con ID: " + idConsultorio, e);
        }
    }


    public List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono FROM medic.\"consultorio\" " +
                request.getConditions() + " OFFSET ? LIMIT ?";
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() -1 ) * size;
        return jdbcTemplate.query(sql, dto.consultorioDTORowMapper, pagina, size);
    }

    public ConsultorioDTO getConsultorio(Integer id) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono FROM medic.\"consultorio\" WHERE idconsultorio = ? ";
        
        return jdbcTemplate.queryForObject(sql, dto.consultorioDTORowMapper, id);
    }

    public int setConsultorio(ConsultorioRequest consultorio) {
        String sql = "CALL medic.\"upsert_consultorio\"(?, ?, ?, ?, ?, ?, ?)";
        int[] resultado = new int[1];
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar consultorio
                    if (consultorio == null) {
                        throw new IllegalArgumentException("El consultorio no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setInt(1, consultorio.getIdconsultorio());
                    cs.setString(2, consultorio.getNombre());
                    cs.setString(3, consultorio.getUbicacion());
                    cs.setString(4, consultorio.getDireccion());
                    cs.setString(5, consultorio.getTelefono());
                    cs.setString(6, consultorio.getRuc());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(7, Types.INTEGER);
                    cs.execute();
                    // Obtener el resultado
                    resultado[0] = cs.getInt(7);
                    return null;
                }
            });
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el consultorio", e);
        }
    }

    public Boolean deleteConsultorio(Integer idConsultorio) {
        String sql = "DELETE FROM medic.\"consultorio\" WHERE idconsultorio = ?";
        int rowsAffected = jdbcTemplate.update(sql, idConsultorio);
        return rowsAffected > 0;
    }
}