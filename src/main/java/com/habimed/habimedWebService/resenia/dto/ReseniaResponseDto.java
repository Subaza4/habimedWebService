package com.habimed.habimedWebService.resenia.dto;

import com.habimed.habimedWebService.usuario.dto.UsuarioResponseDto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReseniaResponseDto {
    private Integer idResenia;
    private Integer idDoctor;
    //private UsuarioResponseDto doctor;
    private BigDecimal calificacion;
    private String comentario;
    private LocalDate fechaResenia;
}