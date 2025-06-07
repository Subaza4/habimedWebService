package com.habimed.habimedWebService.doctorTrabajaConsultorio.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioDTO;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioRequest;
import com.habimed.habimedWebService.doctorTrabajaConsultorio.dto.DoctorTrabajaConsultorioResponse;

@Repository
public class DoctorTrabajaConsultorioRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final DoctorTrabajaConsultorioDTO dto = new DoctorTrabajaConsultorioDTO();

    @Autowired
    public DoctorTrabajaConsultorioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DoctorTrabajaConsultorioResponse> getAllDoctorsTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
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

        sql.append(dto.buildCondition(request.getValuesOfConditions()));

        return jdbcTemplate.query(sql.toString(), dto.doctorTrabajaConsultorioResponseRowMapper());
    }

    public DoctorTrabajaConsultorioResponse getDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
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

    public Integer createDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO medic.\"doctor_trabaja_consultorio\" (\"iddoctor\", \"idconsultorio\") ");
        sql.append("VALUES (?, ?) RETURNING \"iddoctor\", \"idconsultorio\"");

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, request.getIdDoctor(), request.getIdConsultorio());
    }

    public boolean deleteDoctorTrabajaConsultorio(DoctorTrabajaConsultorioRequest request) {
        StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM medic.\"doctor_trabaja_consultorio\" ");
        sql.append(dto.buildCondition(request.getValuesOfConditions()));

        return jdbcTemplate.update(sql.toString()) > 0; // Retorna true si se eliminó al menos un registro
    }
}
