package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import com.habimed.habimedWebService.persona.domain.model.Persona;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioDeleteDto {
    private Persona persona;
    private String correo;
    private String contrasenia;
    private Boolean estado = false;
    private List<HorarioDoctor> horarios;
    private List<Cita> citasComoPaciente;
    private List<Cita> citasComoDoctor;
}