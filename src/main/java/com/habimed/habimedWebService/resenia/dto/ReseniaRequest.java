package com.habimed.habimedWebService.resenia.dto;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaRequest extends RequestREST {
    private Integer idresenia;
    private Integer iddoctor;
    private String calificacion;
    private String comentario;
    private Timestamp fecha_resenia;
}
