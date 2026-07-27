package com.prestamosfacil.application.strategy;

public sealed interface DecisionEvaluacion permits Aprobar, Rechazar, RequerirRevisionManual {
}
