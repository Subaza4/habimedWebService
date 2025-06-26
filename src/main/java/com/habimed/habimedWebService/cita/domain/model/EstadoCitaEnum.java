package com.habimed.habimedWebService.cita.domain.model;

public enum EstadoCitaEnum {
    SOLICITADA,    // Cuando el paciente solicita la cita
    ACEPTADA,      // Cuando el doctor acepta la cita
    PAGADA,        // Cuando se completa el pago
    MODIFICADA,    // Cuando se modifica la cita
    REALIZADA,     // Cuando se completa la cita
    CANCELADA      // Cuando se cancela la cita
}