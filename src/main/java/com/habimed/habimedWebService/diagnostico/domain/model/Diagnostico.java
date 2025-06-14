package com.habimed.habimedWebService.diagnostico.domain.model;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddiagnostico")
    private Integer idDiagnostico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcita", referencedColumnName = "idcita", insertable = false, updatable = false)
    private Cita cita;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_diagnostico", nullable = false)
    private LocalDate fechaDiagnostico = LocalDate.now();
}