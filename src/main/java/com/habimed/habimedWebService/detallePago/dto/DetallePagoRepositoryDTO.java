package com.habimed.habimedWebService.detallePago.dto;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePagoRepositoryDTO {
    private Integer idcita;
    private BigDecimal monto;
    private String metodoPago;
    private String estadoPago;
    private LocalDateTime fechaPago;
    private Integer pagina;
    private Integer tamanioPagina;

    /**
     * Genera condiciones SQL dinámicas basadas en los valores del objeto
     * @return Cadena con las condiciones WHERE
     */
    public String getConditions() {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (idcita != null) {
            conditions.append(" AND \"idcita\" = ").append(idcita);
        }
        if (monto != null) {
            conditions.append(" AND \"monto\" = ").append(monto);
        }
        if (metodoPago != null && !metodoPago.isEmpty()) {
            conditions.append(" AND \"metodo_pago\" LIKE '%").append(metodoPago).append("%'");
        }
        if (estadoPago != null && !estadoPago.isEmpty()) {
            conditions.append(" AND \"estado_pago\" LIKE '%").append(estadoPago).append("%'");
        }
        if (fechaPago != null) {
            conditions.append(" AND \"fecha_pago\" = '").append(fechaPago).append("'");
        }
        return conditions.toString();
    }
}
