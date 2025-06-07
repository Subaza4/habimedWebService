package com.habimed.habimedWebService.doctorTrabajaConsultorio.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorTrabajaConsultorioResponse {
    private int idDoctor;
    private int idConsultorio;
    private String nombreDoctor;
    private String nombreConsultorio;
    private String apellidoDoctor;
    private String rucConsultorio;
}