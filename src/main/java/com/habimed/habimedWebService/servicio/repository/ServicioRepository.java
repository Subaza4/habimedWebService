package com.habimed.habimedWebService.servicio.repository;

import java.util.List;
import java.sql.CallableStatement;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.ConnectionCallback;

import com.habimed.habimedWebService.servicio.dto.ServicioDTO;
import com.habimed.habimedWebService.servicio.dto.ServicioRequest;

@Repository
public class ServicioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private ServicioDTO dto = new ServicioDTO();

    @Autowired
    public ServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ServicioDTO> getAllServicios(ServicioRequest request) {
        String sql = "SELECT s.idservicio, s.nombre, s.descripcion, s.riesgos, s.idespecialidad, e.nombre AS especialidad_nombre, e.descripcion AS especialidad_descripcion "
                   + "FROM medic.\"servicio\" s "
                   + "JOIN medic.\"especialidad\" e ON s.idespecialidad = e.idespecialidad "
                   + request.getConditions() + "OFFSET ? LIMIT ?";
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() - 1) * size;

        return jdbcTemplate.query(sql, dto.getServicioRowMapper(), pagina, size);
    }

    public int setServicio(ServicioRequest servicio) {
        String sql = "CALL medic.\"upsert_servicio\"(?, ?, ?, ?, ?, ?)";
        int[] resultado = new int[1];
        
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar servicio
                    if (servicio == null) {
                        throw new IllegalArgumentException("El servicio no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setObject(1, servicio.getIdservicio(), Types.INTEGER);
                    cs.setObject(2, servicio.getIdespecialidad(), Types.INTEGER);
                    cs.setString(3, servicio.getNombre());
                    cs.setString(4, servicio.getDescripcion());
                    cs.setString(5, servicio.getRiesgos());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(6, Types.INTEGER);
                    cs.execute();
                    // Obtener el resultado
                    resultado[0] = cs.getInt(6);
                    return null;
                }
            });
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el servicio", e);
        }
    }

    public Boolean deleteServicio(Integer idservicio) {
        String sql = "DELETE FROM medic.\"servicio\" WHERE idservicio = ?";
        int rowsAffected = jdbcTemplate.update(sql, idservicio);
        return rowsAffected > 0;
    }
}