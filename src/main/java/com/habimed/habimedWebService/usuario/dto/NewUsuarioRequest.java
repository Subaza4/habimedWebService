package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.persona.dto.PersonaRequest;
import com.habimed.parameterREST.RequestREST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUsuarioRequest {
    private PersonaRequest persona;
    private UsuarioRequest usuario;
}
