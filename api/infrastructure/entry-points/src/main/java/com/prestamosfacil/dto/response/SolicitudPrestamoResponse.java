package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.SolicitudPrestamo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitudPrestamoResponse(Long id, BigDecimal monto, Integer plazoMeses, String estado,
                                         String tipoPrestamoNombre, BigDecimal tasaInteresAnual,
                                         String usuarioNombreCompleto, String usuarioEmail,
                                         BigDecimal usuarioSalarioBase, LocalDateTime fechaSolicitud) {

    public static SolicitudPrestamoResponse from(SolicitudPrestamo solicitud) {
        return new SolicitudPrestamoResponse(solicitud.id(), solicitud.monto(), solicitud.plazoMeses(),
                solicitud.estado().name(), solicitud.tipoPrestamo().nombre(),
                solicitud.tipoPrestamo().tasaInteresAnual(),
                solicitud.usuario().nombres() + " " + solicitud.usuario().apellidos(), solicitud.usuario().email(),
                solicitud.usuario().salarioBase(), solicitud.fechaSolicitud());
    }
}
