package com.habimed.habimedWebService.cita.repository;

import com.habimed.habimedWebService.cita.dto.CitaResponse;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.cita.domain.model.Cita;

@Repository
public class CitaRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final CitaResponse dto = new CitaResponse();

    @Autowired
    public CitaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CitaResponse> getCitas(CitaRequest request) {
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
        List<CitaResponse> citas = jdbcTemplate.query(sql, dto.getCitaRowMapper());
        return citas;
    }

    public CitaResponse getCita(Integer id) {
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

        CitaResponse cita = jdbcTemplate.queryForObject(sql, dto.getCitaRowMapper(), id);
        return cita;
    }

    public Integer setCita(CitaRequest cita) {
        String sql = "INSERT INTO medic.\"cita\" (fecha_hora_inicio, estado, idpaciente, iddoctor) VALUES (?, ?, ?, ?) RETURNING idcita";
        return jdbcTemplate.queryForObject(sql, Integer.class, 
            cita.getFechainicio(), 
            cita.getEstado(), 
            cita.getIdusuario(), 
            cita.getIddoctorconsultorio());
    }

    public boolean deleteCita(Integer id) {
        String sql = "DELETE FROM medic.\"cita\" WHERE idcita = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}
