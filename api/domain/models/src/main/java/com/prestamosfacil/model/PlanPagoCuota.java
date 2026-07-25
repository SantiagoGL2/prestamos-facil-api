package com.prestamosfacil.model;

import java.math.BigDecimal;

public record PlanPagoCuota(Long id, Long prestamoId, Integer numeroCuota, BigDecimal cuota, BigDecimal interes,
                             BigDecimal abonoCapital, BigDecimal saldoPendiente) {
}
