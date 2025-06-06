package com.habimed.habimedWebService.detallePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetallePagoRequest {
    private Long iddetallepago;
    private Integer idcita;
    private BigDecimal monto;
    private String metodopago;
    private String estadopago;
    private LocalDateTime fechapago;

    public String getValuesOfConditions() {
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if (iddetallepago != null) {
            conditions.append(" AND id_detalle_pago = ").append(iddetallepago);
        }
        if (idcita != null) {
            conditions.append(" AND id_cita = ").append(idcita);
        }
        if (monto != null) {
            conditions.append(" AND monto = ").append(monto);
        }
        if (metodopago != null && !metodopago.isEmpty()) {
            conditions.append(" AND metodo_pago = '").append(metodopago).append("'");
        }
        if (estadopago != null && !estadopago.isEmpty()) {
            conditions.append(" AND estado_pago = '").append(estadopago).append("'");
        }
        if (fechapago != null) {
            conditions.append(" AND fecha_pago = '").append(fechapago).append("'");
        }
        return conditions.toString();
    }
}
