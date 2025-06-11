package com.habimed.habimedWebService.resenia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resenia {
    private Integer idresenia;
    private Integer iddoctor;
    private String calificacion;
    private String comentario;
    private Timestamp fecha_resenia;
}
