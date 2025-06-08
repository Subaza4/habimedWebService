package com.habimed.habimedWebService.doctorTrabajaConsultorio.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.CallableStatement;
import java.sql.Types;

@Repository
public class DoctorTrabajaConsultorioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final DoctorTrabajaConsultorioDTO dto = new DoctorTrabajaConsultorioDTO();

    @Autowired
    public DoctorTrabajaConsultorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DoctorTrabajaConsultorioDTO> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("DTC.\"iddoctor\", ");
        sql.append("DTC.\"idconsultorio\", ");
        sql.append("U.\"nombre\" AS \"nombreDoctor\", "); // Alias para el nombre del doctor
        sql.append("U.\"apellido\" AS \"apellidoDoctor\", "); // Alias para el apellido del doctor
        sql.append("C.\"nombre\" AS \"nombreConsultorio\", "); // Alias para el nombre del consultorio
        sql.append("C.\"ruc\" AS \"rucConsultorio\" "); // Alias para el RUC del consultorio
        sql.append("FROM ");
        sql.append("medic.\"doctor_trabaja_consultorio\" DTC "); // Alias para la tabla intermedia
        sql.append("JOIN ");
        sql.append("medic.\"usuario\" U ON DTC.\"iddoctor\" = U.\"dnipersona\" "); // Unión con la tabla de usuarios
        sql.append("JOIN ");
        sql.append("medic.\"consultorio\" C ON DTC.\"idconsultorio\" = C.\"idconsultorio\" ");
        //sql.append(dto.buildCondition(request.getValuesOfConditions()));
        sql.append(request.getConditions("DTC"));

        return jdbcTemplate.query(sql.toString(), dto.doctorTrabajaConsultorioResponseRowMapper());
    }

    /*
    public DoctorTrabajaConsultorioDTO getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("DTC.\"iddoctor\", ");
        sql.append("DTC.\"idconsultorio\", ");
        sql.append("U.\"nombre\" AS \"nombreDoctor\", ");
        sql.append("U.\"apellido\" AS \"apellidoDoctor\", ");
        sql.append("C.\"nombre\" AS \"nombreConsultorio\", ");
        sql.append("C.\"ruc\" AS \"rucConsultorio\" ");
        sql.append("FROM ");
        sql.append("medic.\"doctor_trabaja_consultorio\" DTC ");
        sql.append("JOIN ");
        sql.append("medic.\"usuario\" U ON DTC.\"iddoctor\" = U.\"dnipersona\" ");
        sql.append("JOIN ");
        sql.append("medic.\"consultorio\" C ON DTC.\"idconsultorio\" = C.\"idconsultorio\" ");

        sql.append(dto.buildCondition(request.getValuesOfConditions()));

        return jdbcTemplate.queryForObject(sql.toString(), dto.doctorTrabajaConsultorioResponseRowMapper());
    }
    */

    public Integer setDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        String sql = "CALL medic.\"upsert_doctor_trabaja_consultorio\"(?, ?, ?)";
        int[] resultado = new int[1];
        
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar request
                    if (request == null) {
                        throw new IllegalArgumentException("El request no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setInt(1, request.getIdDoctor());
                    cs.setInt(2, request.getIdConsultorio());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    
                    // Obtener el resultado
                    resultado[0] = cs.getInt(3);
                    return null;
                }
            });
            
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al asignar el doctor al consultorio", e);
        }
    }

    public boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM medic.\"doctor_trabaja_consultorio\" T ");
        sql.append(request.getConditions("T"));

        return jdbcTemplate.update(sql.toString()) > 0; // Retorna true si se eliminó al menos un registro
    }
}