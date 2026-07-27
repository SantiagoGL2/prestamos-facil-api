package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.SolicitudPrestamo;

import org.springframework.stereotype.Component;

@Component
public class EvaluacionManualStrategy implements EvaluacionPrestamoStrategy {

    @Override
    public DecisionEvaluacion evaluar(SolicitudPrestamo solicitud) {
        return new RequerirRevisionManual();
    }
}
