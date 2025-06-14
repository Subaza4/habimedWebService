package com.habimed.habimedWebService.consultorio.domain.model;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "consultorio", schema = "medic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idconsultorio")
    private Integer idConsultorio;

    @Column(name = "ruc", length = 11)
    private String ruc;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @Column(name = "direccion", length = 45)
    private String direccion;

    @Column(name = "telefono", length = 9)
    private String telefono;

    // Relación One-to-Many con Usuario (doctores)
    @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> doctores;

    // Relación Many-to-Many con Servicio
    @ManyToMany
    @JoinTable(
            name = "consultorio_has_servicio",
            schema = "medic",
            joinColumns = @JoinColumn(name = "idconsultorio"),
            inverseJoinColumns = @JoinColumn(name = "idservicio")
    )
    private List<Servicio> servicios;
}