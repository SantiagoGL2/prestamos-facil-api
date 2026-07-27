package com.prestamosfacil.model;

import java.math.BigDecimal;
import java.util.List;

public record ReporteMontosAprobados(List<ReportePrestamoAprobado> prestamos, BigDecimal montoTotalAprobado) {
}
