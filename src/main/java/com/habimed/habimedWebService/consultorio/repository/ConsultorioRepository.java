package com.habimed.habimedWebService.consultorio.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.consultorio.dto.ConsultorioDTO;
import com.habimed.habimedWebService.consultorio.dto.ConsultorioRequest;
import java.sql.CallableStatement;
import java.sql.Types;
import org.springframework.jdbc.core.ConnectionCallback;

@Repository
public class ConsultorioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final ConsultorioDTO dto = new ConsultorioDTO();

    @Autowired
    public ConsultorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ConsultorioDTO> getAllConsultorios(ConsultorioRequest request) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono FROM medic.\"consultorio\" " +
                request.getConditions() + " OFFSET ? LIMIT ?";
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() -1 ) * size;
        return jdbcTemplate.query(sql, dto.consultorioRowMapper, pagina, size);
    }

    public ConsultorioDTO getConsultorio(Integer id) {
        String sql = "SELECT idconsultorio, ruc, nombre, ubicacion, direccion, telefono FROM medic.\"consultorio\" WHERE idconsultorio = ? ";
        
        return jdbcTemplate.queryForObject(sql, dto.consultorioRowMapper, id);
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

    public boolean deleteConsultorio(Integer id) {
        String sql = "DELETE FROM medic.\"consultorio\" WHERE idconsultorio = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}