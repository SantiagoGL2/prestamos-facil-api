package com.prestamosfacil.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportePrestamoAprobado(String tipoPrestamoNombre, LocalDateTime fechaSolicitud, Integer plazoMeses,
                                       BigDecimal montoAprobado) {
}
