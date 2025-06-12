package com.habimed.habimedWebService.detallePago.application;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.parameterREST.ResponseREST;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.detallePago.domain.service.DetallePagoService;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoCreateDto;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/detallePago")
@RequiredArgsConstructor
public class DetallePagoController {

    private final DetallePagoService detallePagoService;

    @GetMapping
    public ResponseEntity<List<DetallePagoCreateDto>> getAllDetallesPago(@RequestBody DetallePagoRequest request) {

        try {
            List<DetallePagoCreateDto> detalles = detallePagoService.getDetallePago(request);
            return ResponseEntity.status(HttpStatus.OK).body(detalles);

        } catch (Exception e) {
            // response.setSalidaMsg("Error al obtener detalles de pago: " + e.getMessage());
            // Otras excepciones pueden manejarse de otra forma.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los detalles de pago", e);
        }
    }

    @PostMapping
    public ResponseEntity<DetallePago> crearOActualizarDetallePago(
            @Valid @RequestBody DetallePagoRequest request) {

        ResponseREST response = new ResponseREST();
        try {
            Integer resultado = detallePagoService.setDetallePago(request);

            switch(resultado) {
                case 1:
                    response.setSalidaMsg("Detalle de pago creado exitosamente");
                    response.setStatus(STATUS_OK);
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                case 2:
                    response.setSalidaMsg("Detalle de pago actualizado exitosamente");
                    response.setStatus(STATUS_OK);
                    return ResponseEntity.ok(response);
                case 3:
                    response.setSalidaMsg("No se puede modificar un pago en estado Pagado");
                    response.setStatus(STATUS_KO);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                default:
                    response.setSalidaMsg("Error al procesar el detalle de pago");
                    response.setStatus(STATUS_KO);
                    return ResponseEntity.internalServerError().body(response);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar detalle de pago: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminarDetallePago(@PathVariable Integer id) {

        try {
            Boolean isDeleted = detallePagoService.deleteDetallePago(id);

            if (isDeleted) {
                //response.setSalidaMsg("Detalle de pago eliminado correctamente");
                return ResponseEntity.ok(isDeleted);
            } else {
                //response.setSalidaMsg("No se pudo eliminar el detalle de pago");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(isDeleted);
            }
        } catch (Exception e) {
            // Otras excepciones pueden manejarse de otra forma.
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar el detalle del pago con id "+id, e);
        }
    }
}