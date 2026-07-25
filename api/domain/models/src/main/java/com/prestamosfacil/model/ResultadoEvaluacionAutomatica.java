package com.prestamosfacil.model;

import java.math.BigDecimal;

public record ResultadoEvaluacionAutomatica(String estadoResultante, BigDecimal capacidadMaxima,
                                             BigDecimal deudaActual, BigDecimal capacidadDisponible,
                                             BigDecimal cuotaNueva) {
}
