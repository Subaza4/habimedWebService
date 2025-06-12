package com.habimed.habimedWebService.detallePago.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetallePagoResponseDto {
    private Integer idDetallePago;
    private Integer idCita;
    private BigDecimal monto;
    private String metodoPago;
    private String estadoPago = "PENDIENTE";
    private LocalDateTime fechaPago;

}
