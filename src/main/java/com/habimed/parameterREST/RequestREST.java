package com.habimed.parameterREST;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestREST {
    private String token;
    private Integer idUsuario;
    private Integer pagina;
    private Integer num_elementos;
}
