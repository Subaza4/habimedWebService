package com.habimed.habimedWebService.servicio.domain.parameter.servicioRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor   // Genera un constructor sin argumentos
@AllArgsConstructor  // Genera un constructor con todos los argumentos
public class ServicioRequest {
    private Integer idservicio; // idservicio INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY

    @NotNull(message = "El ID de especialidad no puede ser nulo.")
    private Integer idespecialidad; // idespecialidad INT NOT NULL (Clave foránea)

    @NotBlank(message = "El nombre del servicio no puede estar vacío.")
    @Size(max = 100, message = "El nombre del servicio debe tener máximo 100 caracteres.")
    private String nombre; // nombre VARCHAR(100) NOT NULL

    @Size(max = 500, message = "La descripción del servicio debe tener máximo 500 caracteres.")
    private String descripcion; // descripcion VARCHAR(500) NULL

    @Size(max = 500, message = "La descripción de riesgos debe tener máximo 500 caracteres.")
    private String riesgos; // riesgos VARCHAR(500) NULL
}
}
