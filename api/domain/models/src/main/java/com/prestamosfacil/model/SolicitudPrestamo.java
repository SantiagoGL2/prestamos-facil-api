package com.prestamosfacil.model;

import com.prestamosfacil.enums.EstadoSolicitud;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitudPrestamo(Long id, Usuario usuario, TipoPrestamo tipoPrestamo, BigDecimal monto,
                                Integer plazoMeses, EstadoSolicitud estado, Long analistaId,
                                LocalDateTime fechaSolicitud, LocalDateTime fechaResolucion) {

    public SolicitudPrestamo conEstado(EstadoSolicitud nuevo) {
        return new SolicitudPrestamo(id, usuario, tipoPrestamo, monto, plazoMeses, nuevo, analistaId,
                fechaSolicitud, fechaResolucion);
    }
}
