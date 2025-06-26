package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CitaResponseDto {
    private Integer idCita;
    private Integer idPaciente;
    private String nombrePaciente;
    private Integer idDoctor;
    private String nombreDoctor;
    private Integer idServicio;
    private String nombreServicio;
    private Integer idConsultorio;
    private String nombreConsultorio;
    private String motivo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private EstadoCitaEnum estado;
    private String descripcion;
    private boolean tienePagoPendiente;
    private Integer idDetallePago;
}