package com.habimed.habimedWebService.recomendacion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionResponseDto {
    private Integer idRecomendacion;
    private Integer idCita;
    private String descripcion;
    private LocalDate fechaRecomendacion;
    private String nombreDoctor;
    private String nombrePaciente;
    private String motivoCita;
}