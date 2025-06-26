package com.habimed.habimedWebService.detallePago.domain.service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.detallePago.domain.model.EstadoPagoEnum;
import com.habimed.habimedWebService.detallePago.domain.model.MetodoPagoEnum;
import com.habimed.habimedWebService.detallePago.dto.DetallePagoResponseDto;
import com.habimed.habimedWebService.detallePago.repository.DetallePagoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final DetallePagoRepository detallePagoRepository;
    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    private static final BigDecimal PORCENTAJE_PLATAFORMA = new BigDecimal("0.20"); // 20%
    private static final BigDecimal PORCENTAJE_PROFESIONAL = new BigDecimal("0.80"); // 80%
    private static final int HORAS_LIMITE_PAGO = 24;

    @Transactional
    public DetallePagoResponseDto crearSolicitudPago(Integer idCita, BigDecimal monto, MetodoPagoEnum metodoPago) {
        // Validar que la cita existe y está en estado ACEPTADA
        Optional<Cita> citaOpt = citaRepository.findById(idCita);
        if (!citaOpt.isPresent()) {
            throw new RuntimeException("Cita no encontrada");
        }

        Cita cita = citaOpt.get();
        if (cita.getEstado() != EstadoCitaEnum.ACEPTADA) {
            throw new RuntimeException("La cita debe estar en estado ACEPTADA para generar el pago");
        }

        // Verificar que no exista ya un pago para esta cita
        if (cita.getDetallePago() != null) {
            throw new RuntimeException("Ya existe un pago asociado a esta cita");
        }

        // Crear el detalle de pago
        DetallePago detallePago = new DetallePago();
        detallePago.setCita(cita);
        detallePago.setMonto(monto);
        detallePago.setMetodoPago(metodoPago);
        detallePago.setEstadoPago(EstadoPagoEnum.PENDIENTE);
        detallePago.setFechaPago(LocalDateTime.now());

        DetallePago saved = detallePagoRepository.save(detallePago);

        return modelMapper.map(saved, DetallePagoResponseDto.class);
    }

    @Transactional
    public DetallePagoResponseDto procesarPago(Integer idDetallePago, String transaccionId) {
        Optional<DetallePago> pagoOpt = detallePagoRepository.findById(idDetallePago);
        if (!pagoOpt.isPresent()) {
            throw new RuntimeException("Detalle de pago no encontrado");
        }

        DetallePago pago = pagoOpt.get();

        // Verificar que el pago esté dentro del límite de 24 horas
        long horasTranscurridas = ChronoUnit.HOURS.between(pago.getFechaPago(), LocalDateTime.now());
        if (horasTranscurridas > HORAS_LIMITE_PAGO) {
            pago.setEstadoPago(EstadoPagoEnum.CANCELADO);
            detallePagoRepository.save(pago);

            // Cancelar la cita
            Cita cita = pago.getCita();
            cita.setEstado(EstadoCitaEnum.CANCELADA);
            citaRepository.save(cita);

            throw new RuntimeException("El tiempo límite para realizar el pago ha expirado");
        }

        // Simular validación con sistema de pago externo (Yape/Plin)
        boolean pagoExitoso = validarPagoExterno(transaccionId, pago.getMetodoPago());

        if (pagoExitoso) {
            pago.setEstadoPago(EstadoPagoEnum.PAGADO);
            pago.setFechaPago(LocalDateTime.now());
            detallePagoRepository.save(pago);

            // Actualizar estado de la cita a PAGADA
            Cita cita = pago.getCita();
            cita.setEstado(EstadoCitaEnum.PAGADA);
            citaRepository.save(cita);

            // Aquí se integraría con Google Calendar para agendar la cita
            agendarEnGoogleCalendar(cita);

            // Calcular distribución del pago
            distribuirPago(pago);

            return modelMapper.map(pago, DetallePagoResponseDto.class);
        } else {
            pago.setEstadoPago(EstadoPagoEnum.RECHAZADO);
            detallePagoRepository.save(pago);
            throw new RuntimeException("El pago fue rechazado");
        }
    }

    @Transactional
    public void procesarCancelacion(Integer idCita) {
        Optional<Cita> citaOpt = citaRepository.findById(idCita);
        if (!citaOpt.isPresent()) {
            throw new RuntimeException("Cita no encontrada");
        }

        Cita cita = citaOpt.get();
        DetallePago pago = cita.getDetallePago();

        if (pago != null && pago.getEstadoPago() == EstadoPagoEnum.PAGADO) {
            // Verificar si aplica penalización
            long horasAnticipacion = ChronoUnit.HOURS.between(LocalDateTime.now(), cita.getFechaHoraInicio());

            if (horasAnticipacion < 24) {
                // Aplicar penalización del 50%
                BigDecimal montoReembolso = pago.getMonto().multiply(new BigDecimal("0.50"));
                procesarReembolso(pago, montoReembolso);
            } else {
                // Reembolso completo
                procesarReembolso(pago, pago.getMonto());
            }
        }

        cita.setEstado(EstadoCitaEnum.CANCELADA);
        citaRepository.save(cita);
    }

    private void procesarReembolso(DetallePago pago, BigDecimal montoReembolso) {
        // Aquí se integraría con el sistema de pagos para realizar el reembolso
        pago.setEstadoPago(EstadoPagoEnum.REEMBOLSADO);
        detallePagoRepository.save(pago);

        // Log del reembolso
        System.out.println("Reembolso procesado: " + montoReembolso + " para el pago ID: " + pago.getIdDetallePago());
    }

    private void distribuirPago(DetallePago pago) {
        BigDecimal montoTotal = pago.getMonto();
        BigDecimal montoPlataforma = montoTotal.multiply(PORCENTAJE_PLATAFORMA);
        BigDecimal montoProfesional = montoTotal.multiply(PORCENTAJE_PROFESIONAL);

        // Aquí se registrarían las transacciones para el profesional y la plataforma
        System.out.println("Distribución del pago ID " + pago.getIdDetallePago() + ":");
        System.out.println("- Monto total: " + montoTotal);
        System.out.println("- Para el profesional (80%): " + montoProfesional);
        System.out.println("- Para la plataforma (20%): " + montoPlataforma);
    }

    private boolean validarPagoExterno(String transaccionId, MetodoPagoEnum metodoPago) {
        // Simulación de validación con sistema externo
        // En producción, aquí se integraría con las APIs de Yape/Plin
        return transaccionId != null && !transaccionId.isEmpty();
    }

    private void agendarEnGoogleCalendar(Cita cita) {
        // Aquí se integraría con Google Calendar API
        System.out.println("Cita agendada en Google Calendar para el doctor: " +
                cita.getDoctor().getPersona().getNombres() + " " +
                cita.getDoctor().getPersona().getApellidos());
    }

    // Método programado para verificar pagos pendientes vencidos
    @Scheduled(fixedDelay = 3600000) // Cada hora
    @Transactional
    public void verificarPagosPendientesVencidos() {
        List<DetallePago> pagosPendientes = detallePagoRepository.findAll().stream()
                .filter(p -> p.getEstadoPago() == EstadoPagoEnum.PENDIENTE)
                .filter(p -> {
                    long horas = ChronoUnit.HOURS.between(p.getFechaPago(), LocalDateTime.now());
                    return horas > HORAS_LIMITE_PAGO;
                })
                .toList();

        for (DetallePago pago : pagosPendientes) {
            pago.setEstadoPago(EstadoPagoEnum.CANCELADO);
            detallePagoRepository.save(pago);

            // Cancelar la cita asociada
            Cita cita = pago.getCita();
            cita.setEstado(EstadoCitaEnum.CANCELADA);
            citaRepository.save(cita);
        }
    }
}