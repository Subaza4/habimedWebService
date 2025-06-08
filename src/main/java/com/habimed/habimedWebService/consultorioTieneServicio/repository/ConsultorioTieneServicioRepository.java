package com.habimed.habimedWebService.consultorioTieneServicio.repository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.ConnectionCallback;
import java.sql.CallableStatement;
import java.sql.Types;

@Repository
public class ConsultorioTieneServicioRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ConsultorioTieneServicioDTO dto = new ConsultorioTieneServicioDTO();

    @Autowired
    public ConsultorioTieneServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

public List<ConsultorioTieneServicioDTO> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT c.\"idconsultorio\", c.\"idservicio\", e.\"idespecialidad\", ")
       .append("s.\"nombre\" as servicio_nombre, e.\"nombre\" as especialidad_nombre ")
       .append("FROM medic.\"consultorio_has_servicio\" c ")
       .append("INNER JOIN medic.\"servicio\" s ON c.\"idservicio\" = s.\"idservicio\" ")
       .append("INNER JOIN medic.\"especialidad\" e ON s.\"idespecialidad\" = e.\"idespecialidad\" ");
    
    if (request != null && !request.getConditions("c").isEmpty()) {
        sql.append(" WHERE ").append(request.getConditions("c"));
    }
    sql.append(" OFFSET ? LIMIT ?");
    Integer size = request.getNum_elementos();
    Integer pagina = (request.getPagina() -1 ) * size;

    return jdbcTemplate.query(sql.toString(), dto.consultorioTieneServicioRowMapper(), pagina, size);
}

    /*public ConsultorioTieneServicioDTO getConsultorioServicioById(ConsultorioTieneServicioRequest request) {
        String sql = "SELECT idconsultoriotienesser, idconsultorio, idservicio FROM medic.\"consultoriotienesser\" ";
        sql += request.getValuesOfConditions();
        return jdbcTemplate.queryForObject(sql, dto.consultorioTieneServicioRowMapper());
    }*/

public Integer setConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
    String sql = "CALL medic.\"upsert_consultorio_has_servicio\"(?, ?, ?)";
    int[] resultado = new int[1];
    
    try {
        jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
            try (CallableStatement cs = connection.prepareCall(sql)) {
                // Validar request
                if (request == null) {
                    throw new IllegalArgumentException("La solicitud no puede ser null");
                }
                // Establecer parámetros de entrada
                cs.setInt(1, request.getIdconsultorio());
                cs.setInt(2, request.getIdservicio());
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
        throw new RuntimeException("Error al asignar servicio al consultorio", e);
    }
}

    public Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        String sql = "DELETE FROM medic.\"consultoriotienesser\" WHERE idconsultorio = ? AND idservicio = ?";
        int rowsAffected = jdbcTemplate.update(sql, request.getIdconsultorio(), request.getIdservicio());
        return rowsAffected > 0;
    }
}