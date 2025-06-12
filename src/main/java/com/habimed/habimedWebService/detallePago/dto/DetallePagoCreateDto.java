package com.habimed.habimedWebService.detallePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePagoCreateDto {

    @NotNull(message = "La cita no puede ser nula")
    private Integer idCita;

    @NotNull(message = "El monto no puede ser nulo")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 enteros y 2 decimales")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Size(max = 50, message = "El método de pago no puede exceder los 50 caracteres")
    private String metodoPago;

    @NotBlank(message = "El estado de pago no puede estar vacío")
    @Size(max = 50, message = "El estado de pago no puede exceder los 50 caracteres")
    private String estadoPago = "PENDIENTE";

    @NotNull(message = "La fecha de pago no puede ser nula")
    private LocalDateTime fechaPago = LocalDateTime.now();
}