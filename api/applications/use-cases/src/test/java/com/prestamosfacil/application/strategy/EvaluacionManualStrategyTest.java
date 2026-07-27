package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.SolicitudPrestamo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EvaluacionManualStrategyTest {

    private final EvaluacionManualStrategy strategy = new EvaluacionManualStrategy();

    @Test
    void evaluarSiempreRetornaRequerirRevisionManual() {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(1L, null, null, BigDecimal.valueOf(5_000_000), 12, null,
                null, null, null);

        DecisionEvaluacion decision = strategy.evaluar(solicitud);

        assertTrue(decision instanceof RequerirRevisionManual);
    }
}
