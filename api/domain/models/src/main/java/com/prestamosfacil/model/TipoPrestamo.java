package com.prestamosfacil.model;

import java.math.BigDecimal;

public record TipoPrestamo(Long id, String nombre, BigDecimal tasaInteresAnual, boolean validacionAutomatica,
                            BigDecimal montoMin, BigDecimal montoMax, Integer plazoMaxMeses, boolean activo) {
}
