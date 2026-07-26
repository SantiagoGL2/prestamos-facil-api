package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.SolicitudPrestamo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitudPrestamoResponse(
        Long id,
        BigDecimal monto,
        Integer plazoMeses,
        String estado,
        TipoPrestamoResumenResponse tipoPrestamo,
        BigDecimal tasaInteresAnual,
        SolicitanteResumenResponse solicitante,
        BigDecimal salarioBase,
        LocalDateTime fechaSolicitud) {

    public static SolicitudPrestamoResponse from(SolicitudPrestamo solicitud) {
        return new SolicitudPrestamoResponse(
                solicitud.id(),
                solicitud.monto(),
                solicitud.plazoMeses(),
                solicitud.estado().name(),
                new TipoPrestamoResumenResponse(solicitud.tipoPrestamo().id(), solicitud.tipoPrestamo().nombre()),
                solicitud.tipoPrestamo().tasaInteresAnual(),
                new SolicitanteResumenResponse(
                        solicitud.usuario().nombres() + " " + solicitud.usuario().apellidos(),
                        solicitud.usuario().email()),
                solicitud.usuario().salarioBase(),
                solicitud.fechaSolicitud());
    }
}
