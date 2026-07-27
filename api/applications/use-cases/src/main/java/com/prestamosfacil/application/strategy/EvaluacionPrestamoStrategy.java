package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.SolicitudPrestamo;

public interface EvaluacionPrestamoStrategy {

    DecisionEvaluacion evaluar(SolicitudPrestamo solicitud);
}
