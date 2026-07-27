package com.prestamosfacil.model;

import java.math.BigDecimal;
import java.util.List;

public record ReporteSolicitadoEvento(String destinatarioEmail, String destinatarioNombre,
                                       List<ReportePrestamoAprobado> prestamos, BigDecimal montoTotalAprobado) {
}
