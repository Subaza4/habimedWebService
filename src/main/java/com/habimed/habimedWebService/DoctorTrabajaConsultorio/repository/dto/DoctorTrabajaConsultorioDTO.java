package com.habimed.habimedWebService.doctorTrabajaConsultorio.repository.dto;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.habimed.habimedWebService.doctorTrabajaConsultorio.domain.parameter.response.DoctorTrabajaConsultorioResponse;

public class DoctorTrabajaConsultorioDTO {
    public RowMapper<DoctorTrabajaConsultorioResponse> doctorTrabajaConsultorioResponseRowMapper() {
        return (rs, rowNum) -> {
            DoctorTrabajaConsultorioResponse response = new DoctorTrabajaConsultorioResponse();
            response.setIdDoctor(rs.getInt("iddoctor")); 
            response.setIdConsultorio(rs.getInt("idconsultorio")); 
            response.setNombreDoctor(rs.getString("nombredoctor"));
            response.setNombreConsultorio(rs.getString("nombreconsultorio"));
            response.setApellidoDoctor(rs.getString("apellidodoctor"));
            response.setRucConsultorio(rs.getString("rucconsultorio"));
            return response;
        };
    }

    public static String buildCondition(Map<String, String> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" WHERE ");
        boolean first = true;
        for (Map.Entry<String, String> entry : conditions.entrySet()) {
            if (!first) {
                sb.append(" AND ");
            }

            if ("idDoctor".equalsIgnoreCase(entry.getKey()) || "idConsultorio".equalsIgnoreCase(entry.getKey())) {
                sb.append(entry.getKey()).append(" = ").append(entry.getValue()); // Asumimos que son INT, no necesitan comillas
            } else {
                System.err.println("Advertencia: Clave de condición no reconocida para DoctorTrabajaConsultorio: " + entry.getKey());
                continue; // Pasa a la siguiente entrada del mapa
            }
            first = false;
        }
        return sb.toString();
    }
}
