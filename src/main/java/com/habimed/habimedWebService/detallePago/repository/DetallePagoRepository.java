package com.habimed.habimedWebService.detallePago.repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoCreateDto;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;

@Repository
public class DetallePagoRepository {
    
    private JdbcTemplate jdbcTemplate;
    private DetallePagoCreateDto dto = new DetallePagoCreateDto();
    
    public DetallePagoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DetallePagoCreateDto> getDetallePago(DetallePagoRequest request) {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "    dp.iddetallepago, " +
            "    dp.monto, " +
            "    dp.metodo_pago, " +
            "    dp.estado_pago, " +
            "    dp.fecha_pago, " +
            "    c.idcita, " +
            "    c.motivo as motivo_cita, " +
            "    c.fecha_hora_inicio as fecha_cita, " +
            "    con.nombre as nombreconsultorio, " +
            "    p_paciente.nombres as nombre_paciente, " +
            "    p_paciente.apellidos as apellido_paciente, " +
            "    p_doctor.nombres as nombre_doctor, " +
            "    p_doctor.apellidos as apellido_doctor " +
            "FROM medic.detalle_pago dp " +
            "INNER JOIN medic.cita c ON dp.idcita = c.idcita " +
            "INNER JOIN medic.usuario u_paciente ON c.idpaciente = u_paciente.idusuario " +
            "INNER JOIN medic.persona p_paciente ON u_paciente.dnipersona = p_paciente.dni " +
            "INNER JOIN medic.usuario u_doctor ON c.iddoctor = u_doctor.idusuario " +
            "INNER JOIN medic.persona p_doctor ON u_doctor.dnipersona = p_doctor.dni " +
            "INNER JOIN medic.doctor_trabaja_consultorio dtc ON u_doctor.idusuario = dtc.iddoctor " +
            "INNER JOIN medic.consultorio con ON dtc.idconsultorio = con.idconsultorio ");

        // Agregar condiciones según los parámetros del request
        if (request != null) {
            sql.append(request.getConditions("dp"));
        }
        sql.append("OFFSET ? LIMIT ?");
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() - 1) * size;

        return jdbcTemplate.query(sql.toString(), dto.detallePagoDTORowMapper(), pagina, size);
    }

    public Integer setDetallePago(DetallePagoRequest request) {
        String sql = "{ call medic.insert_detalle_pago(?, ?, ?, ?, ?, ?) }";
        
        try {
            // Crear un mapa para los parámetros de entrada/salida
            Map<String, Object> inParams = new HashMap<>();
            inParams.put("p_idcita", request.getIdcita());
            inParams.put("p_monto", request.getMonto());
            inParams.put("p_metodo_pago", request.getMetodoPago());
            inParams.put("p_estado_pago", request.getEstadoPago());
            inParams.put("p_fecha_pago", request.getFechaPago());
            inParams.put("p_resultado", Types.INTEGER);

            // Ejecutar el procedimiento almacenado
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("medic")
                .withProcedureName("insert_detalle_pago");

            Map<String, Object> out = jdbcCall.execute(inParams);
            
            // Obtener el resultado
            return (Integer) out.get("p_resultado");
            
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return 3; // Código de error
        }
    }

    public Boolean deleteDetallePago(Integer id) {
        // Implement the logic to delete a DetallePago based on the request
        /* String sql = "DELETE FROM detalle_pago WHERE id_detalle_pago = ?";
        int rowsAffected = jdbcTemplate.update(sql, request.getIddetallepago());
        return rowsAffected > 0; // Retorna true si se eliminó al menos un registro */
        return false;
    }
}