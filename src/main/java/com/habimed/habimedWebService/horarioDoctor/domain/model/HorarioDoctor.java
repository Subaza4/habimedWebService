package com.habimed.habimedWebService.horarioDoctor.domain.model;

import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idhorariodoctor")
    private Integer idHorarioDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddoctor", referencedColumnName = "idusuario", insertable = false, updatable = false)
    private Usuario doctor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaEnum diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalDateTime horaFin;

}