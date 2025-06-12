package com.habimed.habimedWebService.detallePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetallePagoRequest{
    private Integer idDetallePago;
//    private Integer idCita;
//    private BigDecimal monto;
//    private MetodoPagoEnum metodoPago;
//    private EstadoPagoEnum estadoPago;
//    private LocalDateTime fechaPago;
//
//    public String getConditions(String alias) {
//        StringBuilder conditions = new StringBuilder("WHERE 1=1");
//        if (idDetallePago != null) {
//            conditions.append(" AND ").append(alias).append(".\"id_detalle_pago\" = ").append(idCita);
//        }
//        if (idCita != null) {
//            conditions.append(" AND ").append(alias).append(".\"id_cita\" = ").append(idCita);
//        }
//        if (monto != null) {
//            conditions.append(" AND ").append(alias).append(".\"monto\" = ").append(monto);
//        }
//        if (metodoPago != null && !metodoPago.isEmpty()) {
//            conditions.append(" AND ").append(alias).append(".\"metodo_pago\" = '").append(metodoPago).append("'");
//        }
//        if (estadoPago != null && !estadoPago.isEmpty()) {
//            conditions.append(" AND ").append(alias).append(".\"estado_pago\" = '").append(estadoPago).append("'");
//        }
//        if (fechaPago != null) {
//            conditions.append(" AND ").append(alias).append(".\"fecha_pago = '").append(fechaPago).append("'");
//        }
//        return conditions.toString();
//    }
}
