package com.habimed.habimedWebService.detallePago.application;

import com.habimed.parameterREST.ResponseREST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habimed.habimedWebService.detallePago.domain.service.DetallePagoService;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoDTO;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoRequest;
import com.habimed.parameterREST.PeticionREST;

import java.util.List;

@RestController
@RequestMapping("/detallePago")
public class DetallePagoController extends PeticionREST {
    
    private final DetallePagoService detallePagoService;
    
    @Autowired
    public DetallePagoController(DetallePagoService detallePagoService) {
        this.detallePagoService = detallePagoService;
    }

    @PostMapping("getDetallePago")
    public ResponseEntity<ResponseREST> getDetallePago(@RequestBody DetallePagoRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            List<DetallePagoDTO> detallePago = detallePagoService.getDetallePago(request);
            if (detallePago != null) {
                response.setSalida(detallePago);
                response.setSalidaMsg("Detalle de pago encontrado.");
                response.setStatus(STATUS_OK);
            } else {
                response.setSalidaMsg("No se encontraron detalles de pago.");
                response.setStatus(STATUS_KO);
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al obtener el detalle de pago.");
            response.setSalida(e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
    @PostMapping("setDetallePago")
    public ResponseEntity<ResponseREST> setDetallePago(@RequestBody DetallePagoRequest request) {
        ResponseREST response = new ResponseREST();
        try {
            Integer resultado = detallePagoService.setDetallePago(request);
        
            switch (resultado) {
                case 1:
                    response.setSalida(resultado);
                    response.setSalidaMsg("Detalle de pago creado exitosamente.");
                    response.setStatus(STATUS_OK);
                    break;
                case 2:
                    response.setSalida(resultado);
                    response.setSalidaMsg("Detalle de pago actualizado exitosamente.");
                    response.setStatus(STATUS_OK);
                    break;
                case 3:
                    response.setSalida(resultado);
                    response.setSalidaMsg("No se puede modificar un pago que ya está en estado Pagado.");
                    response.setStatus(STATUS_KO);
                    break;
                case 0:
                    response.setSalida(resultado);
                    response.setSalidaMsg("Error en el servidor al procesar el detalle de pago.");
                    response.setStatus(STATUS_KO);
                    break;
                default:
                    response.setSalida(resultado);
                    response.setSalidaMsg("Resultado desconocido al procesar el detalle de pago.");
                    response.setStatus(STATUS_KO);
                    break;
            }
        } catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al procesar el detalle de pago: " + e.getMessage());
            response.setSalida(0);
        }
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("deleteDetallePago")
    public ResponseEntity<ResponseREST> deleteDetallePago(@RequestBody DetallePagoRequest request) {
        ResponseREST response = new ResponseREST();
        try{
            boolean isDeleted = detallePagoService.deleteDetallePago(request);
            if (isDeleted) {
                response.setSalidaMsg("Detalle de pago eliminado correctamente.");
                response.setStatus(STATUS_OK);
            } else {
                response.setSalidaMsg("Error al eliminar el detalle de pago.");
                response.setStatus(STATUS_KO);
            }
        }catch (Exception e) {
            response.setStatus(STATUS_KO);
            response.setSalidaMsg("Error al eliminar el detalle de pago.");
            response.setSalida(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}