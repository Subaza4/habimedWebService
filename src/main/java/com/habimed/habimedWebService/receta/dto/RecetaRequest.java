package com.habimed.habimedWebService.receta.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.habimed.parameterREST.RequestREST;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetaRequest extends RequestREST {

    private Long idReceta;
    private Long idCita;
    @Size(max = 1000)
    private String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReceta;
}
