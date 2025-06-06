package com.habimed.parameterREST;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseREST {
    private String Status;
    private Object Salida;
    private String SalidaMsg;

    public String getStatus() { return Status; }
    public void setStatus(String status) { this.Status = status; }
    public Object getSalida() { return Salida; }
    public void setSalida(Object salida) { this.Salida = salida; }
    public String getSalidaMsg() { return SalidaMsg; }
    public void setSalidaMsg(String salidaMsg) { this.SalidaMsg = salidaMsg; }
}
