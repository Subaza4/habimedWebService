package com.habimed.habimedWebService.consultorio.domain.model;

import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultorio_tiene_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ConsultorioServicioId.class)
public class ConsultorioTieneServicio {

    @Id
    @ManyToOne
    @JoinColumn(name = "idconsultorio", referencedColumnName = "idconsultorio")
    private Consultorio consultorio;

    @Id
    @ManyToOne
    @JoinColumn(name = "idservicio", referencedColumnName = "idservicio")
    private Servicio servicio;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;
}

// Clase para la clave compuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
class ConsultorioServicioId implements java.io.Serializable {
    private Integer consultorio;
    private Integer servicio;
}
