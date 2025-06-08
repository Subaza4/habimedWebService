package com.habimed.habimedWebService.horarioDoctor.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorRequest;
import com.habimed.habimedWebService.horarioDoctor.dto.HorarioDoctorDTO;

import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.CallableStatement;
import java.sql.Types;

@Repository
public class HorarioDoctorRepository {

    private final JdbcTemplate jdbcTemplate;
    private final HorarioDoctorDTO dto = new HorarioDoctorDTO();

    @Autowired
    public HorarioDoctorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<HorarioDoctorDTO> getAllHorarios(HorarioDoctorRequest request) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT hd.idhorariodoctor, hd.iddoctor, p.nombres, p.apellidos, p.correo, ")
           .append("hd.dia_semana, hd.hora_inicio, hd.hora_fin, hd.duracion_minutos ")
           .append("FROM medic.\"horario_doctor\" hd ")
           .append("INNER JOIN medic.\"usuario\" u ON hd.iddoctor = u.idusuario ")
           .append("INNER JOIN medic.\"persona\" p ON u.dnipersona = p.dni ")
           .append("WHERE u.tipousuario = 3 "); // Filtro para asegurarnos que solo sean doctores
        // Agregar condiciones adicionales desde el request si existen
        if (request.getConditions() != null && !request.getConditions().isEmpty()) {
            sql.append(" AND ").append(request.getConditions());
        }
        // Agregar ordenamiento
        sql.append(" ORDER BY hd.dia_semana, hd.hora_inicio");
        // Agregar paginación
        if (request.getPagina() != null && request.getNum_elementos() != null) {
            int offset = (request.getPagina() - 1) * request.getNum_elementos();
            sql.append(" OFFSET ").append(offset)
               .append(" LIMIT ").append(request.getNum_elementos());
        }
        return jdbcTemplate.query(sql.toString(), dto.horarioDoctorRowMapper());
    }

    /*public HorarioDoctorDTO getHorarioById(Integer idhorariodoctor) {
        String sql = "SELECT * FROM medic.\"horariodoctor\" WHERE idhorariodoctor = ?";
        return jdbcTemplate.queryForObject(sql, dto.horarioDoctorRowMapper(), idhorariodoctor);
    }*/

    public Integer setHorario(HorarioDoctorRequest request) {
        String sql = "CALL medic.\"upsert_horario_doctor\"(?, ?, ?, ?, ?, ?, ?)";
        int[] resultado = new int[1];
        
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar request
                    if (request == null) {
                        throw new IllegalArgumentException("El request no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setObject(1, request.getIdhorariodoctor(), Types.INTEGER);
                    cs.setInt(2, request.getIddoctor());
                    cs.setString(3, request.getDiaSemana());
                    cs.setTime(4, request.getHoraInicio());
                    cs.setTime(5, request.getHoraFin());
                    cs.setInt(6, request.getDuracionMinutos());
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
            throw new RuntimeException("Error al guardar el horario del doctor", e);
        }
    }

    public boolean deleteHorario(Integer idhorariodoctor) {
        String sql = "DELETE FROM medic.\"horariodoctor\" WHERE idhorariodoctor = ?";
        return jdbcTemplate.update(sql, idhorariodoctor) > 0;
    }
}