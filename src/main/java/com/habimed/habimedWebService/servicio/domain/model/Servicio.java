package com.habimed.habimedWebService.servicio.domain.model;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idservicio")
    private Integer idServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idespecialidad", referencedColumnName = "idespecialidad", insertable = false, updatable = false)
    private Especialidad especialidad;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "riesgos", length = 500)
    private String riesgos;

    @ManyToMany(mappedBy = "servicios")
    private List<Consultorio> consultorios;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citas;
}