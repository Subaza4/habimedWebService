package com.habimed.habimedWebService.detallePago.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.habimed.habimedWebService.cita.domain.model.Cita;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetallepago")
    private Integer idDetallePago;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcita", referencedColumnName = "idcita", insertable = false, updatable = false)
    private Cita cita;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", nullable = false)
    @Enumerated(EnumType.STRING)
    private MetodoPagoEnum metodoPago;

    @Column(name = "estado_pago", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estadoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();
}

