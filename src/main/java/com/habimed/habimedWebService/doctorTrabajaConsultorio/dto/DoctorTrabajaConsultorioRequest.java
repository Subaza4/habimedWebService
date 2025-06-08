package com.habimed.habimedWebService.doctorTrabajaConsultorio.dto;

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

    public String getConditions(String alias){
        StringBuilder conditions = new StringBuilder("WHERE 1=1");
        if(idDoctor > 0)
            conditions.append(" AND ").append(alias).append(".\"iddoctor\" = ").append(idDoctor);
        if(idConsultorio > 0)
            conditions.append(" AND ").append(alias).append(".\"idconsultorio\" = ").append(idConsultorio);
        return conditions.toString();
    }
}
