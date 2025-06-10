package com.habimed.habimedWebService.permisoHistorial.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.el.parser.BooleanNode;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermisosHistorial {
    /*
     otorgar permiso a todo el historial del paciente le permite al doctor
     Saber en qué consultorios se atendió
     Consultar sus citas filtradas por consultorio
     Revisar las recetas, recomendaciones, diagnosticos y detalles de la cita
     */
    public Integer idpermisohistorial;
    public Integer iddoctor;
    public Integer idpaciente;
    public Timestamp fechaotorgapermiso;
    public Timestamp fechadeniegapermiso;
    public BooleanNode estado;
}
