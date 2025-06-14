package com.habimed.habimedWebService.permisoHistorial.domain.model;

import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisosHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpermisohistorial")
    private Integer idPermisoHistorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddoctor", referencedColumnName = "idusuario", insertable = false, updatable = false)
    private Usuario doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpaciente", referencedColumnName = "idusuario", insertable = false, updatable = false)
    private Usuario paciente;

    @Column(name = "fechaotorgapermiso", nullable = false)
    private LocalDate fechaOtorgaPermiso = LocalDate.now();

    @Column(name = "fechadeniegapermiso")
    private LocalDate fechaDenegaPermiso;

    @Column(name = "estado", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private EstadoPermisosEnum estado;
}
