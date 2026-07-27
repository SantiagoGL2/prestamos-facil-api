package com.prestamosfacil.application.strategy;

import java.math.BigDecimal;

public record Aprobar(BigDecimal montoAprobado) implements DecisionEvaluacion {
}
