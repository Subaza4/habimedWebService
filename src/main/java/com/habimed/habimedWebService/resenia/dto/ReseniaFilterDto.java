package com.habimed.habimedWebService.resenia.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReseniaFilterDto {
    private Integer idresenia;
    private Integer iddoctor;
    private Double calificacion;
    private String comentario;
    private LocalDate fecha_resenia;
}
