package com.habimed.habimedWebService.detallePago.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
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

    public DetallePagoDTO findAll(DetallePagoRequest request) {
        // Implement the logic to retrieve all DetallePago based on the request
        String sql = "SELECT * FROM detalle_pago WHERE ..."; // Add your SQL query here
        return jdbcTemplate.query(sql, dto.detallePagoDTORowMapper())
                .stream()
                .findFirst()
                .orElse(null); // Return null if no records found, or handle as needed
    }

    public Integer setDetallePago(DetallePagoRequest request) {
        // La llamada a un stored procedure en PostgreSQL se hace con la sintaxis {call procedure_name(...)}
        String sql = "{ call medic.insert_detalle_pago(?, ?, ?, ?, ?) }";

        // Los parámetros se deben pasar en el orden definido por el stored procedure.
        // Asegúrate de que los tipos de datos de Java coincidan o sean compatibles con los de PostgreSQL.
        return jdbcTemplate.update(
            sql,
            request.getIdcita(),             // p_idCita INT
            request.getMonto(),              // p_monto DECIMAL
            request.getMetodopago(),         // p_metodo_pago VARCHAR
            request.getEstadopago(),         // p_estado_pago VARCHAR (puede ser null si quieres usar el DEFAULT del SP)
            request.getFechapago()           // p_fecha_pago TIMESTAMP (puede ser null si quieres usar el DEFAULT del SP)
        );
    }

    public boolean deleteDetallePago(DetallePagoRequest request) {
        // Implement the logic to delete a DetallePago based on the request
        /* String sql = "DELETE FROM detalle_pago WHERE id_detalle_pago = ?";
        int rowsAffected = jdbcTemplate.update(sql, request.getIddetallepago());
        return rowsAffected > 0; // Retorna true si se eliminó al menos un registro */
        return false;
    }
}
