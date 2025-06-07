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
        DetallePagoDTO detallePago = detallePagoService.getDetallePago(request);
        if (detallePago != null) {
            response.setSalida(detallePago);
            response.setSalidaMsg("Detalle de pago encontrado.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("No se encontraron detalles de pago.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }
    @PostMapping("setDetallePago")
    public ResponseEntity<ResponseREST> setDetallePago(@RequestBody DetallePagoRequest request) {
        ResponseREST response = new ResponseREST();
        Integer idDetallePago = detallePagoService.setDetallePago(request);
        if (idDetallePago != null) {
            response.setSalida(idDetallePago);
            response.setSalidaMsg("Detalle de pago creado o actualizado correctamente.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("Error al crear o actualizar el detalle de pago.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }
    @PostMapping("deleteDetallePago")
    public ResponseEntity<ResponseREST> deleteDetallePago(@RequestBody DetallePagoRequest request) {
        ResponseREST response = new ResponseREST();
        boolean isDeleted = detallePagoService.deleteDetallePago(request);
        if (isDeleted) {
            response.setSalidaMsg("Detalle de pago eliminado correctamente.");
            response.setStatus(STATUS_OK);
        } else {
            response.setSalidaMsg("Error al eliminar el detalle de pago.");
            response.setStatus(STATUS_KO);
        }
        return ResponseEntity.ok(response);
    }
}
