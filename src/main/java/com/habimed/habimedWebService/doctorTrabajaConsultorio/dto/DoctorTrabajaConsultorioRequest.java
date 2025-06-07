package com.habimed.habimedWebService.doctorTrabajaConsultorio.dto;

import java.util.Map;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorTrabajaConsultorioRequest extends RequestREST {
    private int idDoctor;
    private int idConsultorio;

    public Map<String, String> getValuesOfConditions(){
        Map<String, String> conditions = new java.util.HashMap<>();
        if(idDoctor > 0)
            conditions.put("iddoctor", String.valueOf(idDoctor));
        if(idConsultorio > 0)
            conditions.put("idconsultorio", String.valueOf(idConsultorio));

        return conditions;
    }
}
