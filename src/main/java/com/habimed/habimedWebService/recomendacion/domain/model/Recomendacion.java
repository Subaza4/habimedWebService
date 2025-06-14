package com.habimed.habimedWebService.recomendacion.domain.model;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrecomendacion")
    private Integer idRecomendacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcita", referencedColumnName = "idcita", insertable = false, updatable = false)
    private Cita cita;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_recomendacion", nullable = false)
    private LocalDate fechaRecomendacion = LocalDate.now();
}