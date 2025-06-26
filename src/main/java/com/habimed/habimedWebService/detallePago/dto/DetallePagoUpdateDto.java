package com.habimed.habimedWebService.detallePago.dto;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.detallePago.domain.model.EstadoPagoEnum;
import com.habimed.habimedWebService.detallePago.domain.model.MetodoPagoEnum;
import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetallePagoUpdateDto {
    private Integer idDetallePago;

    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal monto;

    private MetodoPagoEnum metodoPago;

    private EstadoPagoEnum estadoPago;

    private LocalDateTime fechaPago;

    private Cita cita;
}