package com.habimed.habimedWebService.detallePago.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.habimed.habimedWebService.cita.domain.model.Cita;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetallePago {
    private Long idDetallePago;
    private Cita cita;
    private BigDecimal monto;
    private String metodoPago;
    private String estadoPago;
    private LocalDateTime fechaPago;
}
