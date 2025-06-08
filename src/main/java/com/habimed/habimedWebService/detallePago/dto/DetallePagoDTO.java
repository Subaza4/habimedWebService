package com.habimed.habimedWebService.detallePago.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePagoDTO {
    private Long idDetallePago;
    private Long idCita; // Solo el ID de la cita para referencia
    private String motivoCita; // Campo de Cita
    private LocalDateTime fechaCita; // Campo de Cita
    private String nombreconsultorio;
    private BigDecimal monto;
    private String metodoPago;
    private String estadoPago;
    private LocalDateTime fechaPago;
    private String nombrePaciente; // Campo de Persona (a través de Cita y usuario)
    private String apellidoPaciente; // Campo de Persona (a través de Cita y usuario)
    private String nombreDoctor; // Campo de Persona (a través de Cita y usuario)
    private String apellidoDoctor;

    public RowMapper<DetallePagoDTO> detallePagoDTORowMapper() {
        return (rs, rowNum) -> {
            DetallePagoDTO detallePago = new DetallePagoDTO();
            detallePago.setIdDetallePago(rs.getLong("iddetallepago"));
            detallePago.setIdCita(rs.getLong("idcita"));
            detallePago.setMotivoCita(rs.getString("motivo_cita"));
            detallePago.setFechaCita(rs.getObject("fecha_cita", LocalDateTime.class));
            detallePago.setNombreconsultorio(rs.getString("nombreconsultorio"));
            detallePago.setMonto(rs.getBigDecimal("monto"));
            detallePago.setMetodoPago(rs.getString("metodo_pago"));
            detallePago.setEstadoPago(rs.getString("estado_pago"));
            detallePago.setFechaPago(rs.getObject("fecha_pago", LocalDateTime.class));
            detallePago.setNombrePaciente(rs.getString("nombre_paciente"));
            detallePago.setApellidoPaciente(rs.getString("apellido_paciente"));
            detallePago.setNombreDoctor(rs.getString("nombre_doctor"));
            detallePago.setApellidoDoctor(rs.getString("apellido_doctor"));
            return detallePago;
        };
    }
}