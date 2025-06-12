package com.habimed.habimedWebService.detallePago.repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRepositoryDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.detallePago.dto.DetallePagoDTO;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;

@Repository
public class DetallePagoRepository {
    
    private JdbcTemplate jdbcTemplate;
    private DetallePagoDTO dto = new DetallePagoDTO();
    
    public DetallePagoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DetallePago> findAllDetallePago(){
        String sql = "SELECT * FROM medic.\"detalle_pago\" ORDER BY \"iddetallepago\" DESC ";
        return jdbcTemplate.query(sql, dto.detallePagoRowMapper());
    }

    public List<DetallePago> findAllDetallePago(DetallePagoRepositoryDTO request){
        String sql = "SELECT * FROM medic.\"detalle_pago\" " +
                request.getConditions() + " ORDER BY \"iddetallepago\" DESC LIMIT ? OFFSET ? ";
        Integer tamanioPagina = request.getTamanioPagina();
        Integer pagina = (request.getPagina() - 1) * tamanioPagina;

        return jdbcTemplate.query(sql, dto.detallePagoRowMapper(),tamanioPagina,pagina);
    }

    public DetallePago findByIdDetallePago(Integer idDetallePago) {
        String sql = "SELECT * FROM medic.\"detalle_pago\" WHERE iddetallepago = ?";
        List<DetallePago> detallePagos = jdbcTemplate.query(sql, dto.detallePagoRowMapper(), idDetallePago);
        if (detallePagos.isEmpty()) {
            return null;
        } else {
            return detallePagos.get(0);
        }
    }

    public DetallePago insertDetallePago(DetallePago detallePago) {
        if (detallePago == null) {
            throw new IllegalArgumentException("El detalle de pago no puede ser nulo");
        }
        if (detallePago.getIdcita() == null) {
            throw new IllegalArgumentException("El ID de cita es obligatorio");
        }
        if (detallePago.getMonto() == null) {
            throw new IllegalArgumentException("El monto es obligatorio");
        }
        if (detallePago.getMetodoPago() == null || detallePago.getMetodoPago().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
        String sql = "INSERT INTO medic.\"detalle_pago\" (idcita, monto, metodo_pago, estado_pago, fecha_pago) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING iddetallepago";

        try {
            // Si no se proporciona el estado de pago, usar el valor predeterminado 'Pendiente'
            String estadoPago = detallePago.getEstadoPago();
            if (estadoPago == null || estadoPago.isEmpty()) {
                estadoPago = "Pendiente";
            }

            // Si no se proporciona la fecha de pago, usar la fecha actual
            LocalDateTime fechaPago = detallePago.getFechaPago();
            if (fechaPago == null) {
                fechaPago = LocalDateTime.now();
            }

            // Ejecutar la inserción y obtener el ID generado
            Long idDetallePago = jdbcTemplate.queryForObject(
                    sql,
                    Long.class,
                    detallePago.getIdcita(),
                    detallePago.getMonto(),
                    detallePago.getMetodoPago(),
                    estadoPago,
                    java.sql.Timestamp.valueOf(fechaPago)
            );

            // Crear un nuevo objeto con el ID generado y los demás datos
            DetallePago detallePagoInsertado = new DetallePago();
            detallePagoInsertado.setIdDetallePago(idDetallePago);
            detallePagoInsertado.setIdcita(detallePago.getIdcita());
            detallePagoInsertado.setMonto(detallePago.getMonto());
            detallePagoInsertado.setMetodoPago(detallePago.getMetodoPago());
            detallePagoInsertado.setEstadoPago(estadoPago);
            detallePagoInsertado.setFechaPago(fechaPago);

            return detallePagoInsertado;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al insertar el detalle de pago: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un detalle de pago existente en la base de datos
     * @param detallePago El detalle de pago con los datos actualizados
     * @return El detalle de pago actualizado o null si no se encontró
     * @throws IllegalArgumentException Si detallePago es nulo o no tiene ID
     * @throws RuntimeException Si ocurre un error durante la actualización
     */
    public DetallePago updateDetallePago(DetallePagoRepositoryDTO detallePago, Integer idDetallePago) {
        if (detallePago == null) {
            throw new IllegalArgumentException("El detalle de pago no puede ser nulo");
        }
        if (idDetallePago == null) {
            throw new IllegalArgumentException("El ID del detalle de pago es obligatorio para actualizarlo");
        }
        String sql = "UPDATE medic.\"detalle_pago\" SET " +
                "idcita = ?, monto = ?, metodo_pago = ?, estado_pago = ?, fecha_pago = ? " +
                "WHERE iddetallepago = ?";

        try {
            // Si no se proporciona la fecha de pago, mantener la existente
            LocalDateTime fechaPago = detallePago.getFechaPago();
            if (fechaPago == null) {
                // Consultar la fecha actual en la base de datos
                String selectSql = "SELECT fecha_pago FROM medic.\"detalle_pago\" WHERE iddetallepago = ?";
                java.sql.Timestamp timestamp = jdbcTemplate.queryForObject(
                        selectSql, java.sql.Timestamp.class, idDetallePago);
                if (timestamp != null) {
                    fechaPago = timestamp.toLocalDateTime();
                } else {
                    fechaPago = LocalDateTime.now();
                }
            }
            // Ejecutar la actualización
            int filasActualizadas = jdbcTemplate.update(
                    sql,
                    detallePago.getIdcita(),
                    detallePago.getMonto(),
                    detallePago.getMetodoPago(),
                    detallePago.getEstadoPago(),
                    java.sql.Timestamp.valueOf(fechaPago),
                    idDetallePago
            );

            // Si se actualizó correctamente, devolver el objeto con todos sus datos
            if (filasActualizadas > 0) {
                DetallePago detallePagoActualizado = new DetallePago();
                detallePagoActualizado.setIdDetallePago(idDetallePago.longValue());
                detallePagoActualizado.setIdcita(detallePago.getIdcita());
                detallePagoActualizado.setMonto(detallePago.getMonto());
                detallePagoActualizado.setMetodoPago(detallePago.getMetodoPago());
                detallePagoActualizado.setEstadoPago(detallePago.getEstadoPago());
                detallePagoActualizado.setFechaPago(fechaPago);

                return detallePagoActualizado;
            } else {
                // No se encontró el detalle de pago con ese ID
                return null;
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al actualizar el detalle de pago: " + e.getMessage(), e);
        }
    }

    public List<DetallePagoDTO> getDetallePago(DetallePagoRequest request) {
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
            inParams.put("p_metodo_pago", request.getMetodopago());
            inParams.put("p_estado_pago", request.getEstadopago());
            inParams.put("p_fecha_pago", request.getFechapago());
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

    public Boolean deleteDetallePago(DetallePagoRequest request) {
        // Implement the logic to delete a DetallePago based on the request
        /* String sql = "DELETE FROM detalle_pago WHERE id_detalle_pago = ?";
        int rowsAffected = jdbcTemplate.update(sql, request.getIddetallepago());
        return rowsAffected > 0; // Retorna true si se eliminó al menos un registro */
        return false;
    }

    public Boolean deleteDetallePago(Integer id) {
        // Implement the logic to delete a DetallePago based on the request
        String sql = "DELETE FROM detalle_pago WHERE id_detalle_pago = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
}