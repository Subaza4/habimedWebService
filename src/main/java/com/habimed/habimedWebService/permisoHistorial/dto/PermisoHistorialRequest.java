package com.habimed.habimedWebService.permisoHistorial.dto;

import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermisoHistorialRequest extends RequestREST {
    /*
    Para otorgar permiso hace falta el token del usuario, con ello capturar su id
    El id del doctor a quien compartirá su historial, establecer el rango de fechas en que desea compartir y estado true
    Para retirar un permiso necesita enviar su token, el id del doctor y un estado en false

    Para consultar el historial del paciente
    -Con el id del doctor apuntar a un controlador que siempre consulte si el usuario (token -> idusuario) tiene aún
        permiso para consultar dicha información
    -Consultar los consultorios a los que asistió el paciente
    -Consultar las citas del paciente en dicho consultorio
    -Consultar el detalle de la cita del paciente en dicho consultorio
    -Consultar las recetas, recomendaciones y diagnosticos
     */

    private Integer idpermisohistorial;
    private Integer iddoctor;
    private Integer idpaciente;
    private String fechaotorgapermiso;
    private String fechadeniegapermiso;
    private Boolean estado;
}
