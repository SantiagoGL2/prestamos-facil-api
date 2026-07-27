package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.ReportePrestamoAprobado;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportePrestamoAprobadoResponse(String tipoPrestamoNombre, LocalDateTime fechaSolicitud,
                                               Integer plazoMeses, BigDecimal montoAprobado) {

    public static ReportePrestamoAprobadoResponse from(ReportePrestamoAprobado prestamo) {
        return new ReportePrestamoAprobadoResponse(prestamo.tipoPrestamoNombre(), prestamo.fechaSolicitud(),
                prestamo.plazoMeses(), prestamo.montoAprobado());
    }
}
