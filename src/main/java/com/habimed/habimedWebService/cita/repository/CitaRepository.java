package com.habimed.habimedWebService.cita.repository;

import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.sql.Types;

@Repository
public class CitaRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final CitaDTO dto = new CitaDTO();

    @Autowired
    public CitaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CitaDTO> getCitas(CitaRequest request) {
        String sql = "SELECT " +
                "c.\"idcita\", " +
                "c.\"fecha_hora_inicio\" AS \"fechacita\", " +
                "c.\"estado\", " +
                "dp.\"estado_pago\" AS \"estadopago\", " +
                "u.\"dnipersona\" AS \"idusuario\", " +
                "CONCAT(p.\"nombres\", ' ', p.\"apellidos\") AS \"nombreusuario\", " +
                "dp.\"iddetallepago\", " +
                "dp.\"monto\" AS \"montopago\", " +
                "cons.\"nombre\" AS \"nombreconsultorio\", " +
                "cons.\"direccion\" AS \"direccionconsultorio\", " +
                "d.\"nombres\" AS \"nombredoctor\" " +
            "FROM medic.\"cita\" c " +
            "JOIN medic.\"usuario\" u ON c.\"idpaciente\" = u.\"dnipersona\" " +
            "JOIN medic.\"persona\" p ON u.\"dnipersona\" = p.\"dni\" " +
            "LEFT JOIN medic.\"detalle_pago\" dp ON c.\"idcita\" = dp.\"idcita\" " +
            "LEFT JOIN medic.\"doctor_trabaja_consultorio\" dtc ON c.\"iddoctor\" = dtc.\"iddoctor\" " +
            "LEFT JOIN medic.\"consultorio\" cons ON dtc.\"idconsultorio\" = cons.\"idconsultorio\" " +
            "LEFT JOIN medic.\"persona\" d ON c.\"iddoctor\" = d.\"dni\" ";
        sql += request.getValuesOfConditions();
        List<CitaDTO> citas = jdbcTemplate.query(sql, dto.getCitaRowMapper());
        return citas;
    }

    public CitaDTO getCita(Integer id) {
        String sql = "SELECT " +
                "c.\"idcita\", " +
                "c.\"fecha_hora_inicio\" AS \"fechacita\", " +
                "c.\"estado\", " +
                "dp.\"estado_pago\" AS \"estadopago\", " +
                "u.\"dnipersona\" AS \"idusuario\", " +
                "CONCAT(p.\"nombres\", ' ', p.\"apellidos\") AS \"nombreusuario\", " +
                "dp.\"iddetallepago\", " +
                "dp.\"monto\" AS \"montopago\", " +
                "cons.\"nombre\" AS \"nombreconsultorio\", " +
                "cons.\"direccion\" AS \"direccionconsultorio\", " +
                "d.\"nombres\" AS \"nombredoctor\" " +
            "FROM medic.\"cita\" c " +
            "JOIN medic.\"usuario\" u ON c.\"idpaciente\" = u.\"dnipersona\" " +
            "JOIN medic.\"persona\" p ON u.\"dnipersona\" = p.\"dni\" " +
            "LEFT JOIN medic.\"detalle_pago\" dp ON c.\"idcita\" = dp.\"idcita\" " +
            "LEFT JOIN medic.\"doctor_trabaja_consultorio\" dtc ON c.\"iddoctor\" = dtc.\"iddoctor\" " +
            "LEFT JOIN medic.\"consultorio\" cons ON dtc.\"idconsultorio\" = cons.\"idconsultorio\" " +
            "LEFT JOIN medic.\"persona\" d ON c.\"iddoctor\" = d.\"dni\" WHERE c.idcita = ?";

        CitaDTO cita = jdbcTemplate.queryForObject(sql, dto.getCitaRowMapper(), id);
        return cita;
    }

    public Integer setCita(CitaRequest cita) {
        String sql = "CALL medic.\"upsert_cita\"(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int[] resultado = new int[1];
        
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar cita
                    if (cita == null) {
                        throw new IllegalArgumentException("La cita no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setObject(1, cita.getIdcita(), Types.INTEGER);
                    cs.setInt(2, cita.getIdusuario());
                    cs.setInt(3, cita.getIddoctorconsultorio());
                    cs.setString(4, cita.getMotivo());
                    cs.setTimestamp(5, cita.getFechainicio());
                    cs.setTimestamp(6, cita.getFechafin());
                    cs.setString(7, cita.getEstado());
                    cs.setString(8, cita.getDescripcion());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(9, Types.INTEGER);
                    cs.execute();
                    // Obtener el resultado
                    resultado[0] = cs.getInt(9);
                    return null;
                }
            });
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la cita", e);
        }
    }

    public boolean deleteCita(Integer id, Integer idDoctor) {
        String sql = "DELETE FROM medic.\"cita\" WHERE idcita = ? AND iddoctor = ?";
        int rowsAffected = jdbcTemplate.update(sql, id, idDoctor);
        return rowsAffected > 0;
    }
}