package com.habimed.habimedWebService.cita.domain.model;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcita")
    private Integer idCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpaciente", referencedColumnName = "idusuario", nullable = false)
    private Usuario paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddoctor", referencedColumnName = "idusuario", nullable = false)
    private Usuario doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", referencedColumnName = "idservicio", nullable = false)
    private Servicio servicio;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDateTime fechaHoraFin;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCitaEnum estado = EstadoCitaEnum.SOLICITADA;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    // Relaciones inversas
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagnostico> diagnosticos;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Receta> recetas;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recomendacion> recomendaciones;

    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DetallePago detallePago;
}