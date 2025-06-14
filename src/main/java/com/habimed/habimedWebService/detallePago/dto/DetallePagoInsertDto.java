package com.habimed.habimedWebService.detallePago.dto;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.detallePago.domain.model.EstadoPagoEnum;
import com.habimed.habimedWebService.detallePago.domain.model.MetodoPagoEnum;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetallePagoInsertDto {
    private Integer idDetallePago;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal monto;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPagoEnum metodoPago;

    @NotNull(message = "El estado de pago es obligatorio")
    private EstadoPagoEnum estadoPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;

    @NotNull(message = "La cita es obligatoria")
    private Cita cita;
}