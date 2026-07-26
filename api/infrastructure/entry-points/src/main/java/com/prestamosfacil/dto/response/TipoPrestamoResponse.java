package com.prestamosfacil.dto.response;

import com.prestamosfacil.model.TipoPrestamo;

import java.math.BigDecimal;

public record TipoPrestamoResponse(Long id, String nombre, BigDecimal tasaInteresAnual, BigDecimal montoMin,
                                    BigDecimal montoMax, Integer plazoMaxMeses, boolean validacionAutomatica) {

    public static TipoPrestamoResponse from(TipoPrestamo tipoPrestamo) {
        return new TipoPrestamoResponse(tipoPrestamo.id(), tipoPrestamo.nombre(), tipoPrestamo.tasaInteresAnual(),
                tipoPrestamo.montoMin(), tipoPrestamo.montoMax(), tipoPrestamo.plazoMaxMeses(),
                tipoPrestamo.validacionAutomatica());
    }
}
