package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.SolicitudPrestamo;

/**
 * Contrato del patrón Strategy usado para decidir qué pasa con una solicitud recién
 * registrada: evaluarla automáticamente o dejarla para revisión manual, según lo que tenga
 * configurado el tipo de préstamo.
 */
public interface EvaluacionPrestamoStrategy {

    DecisionEvaluacion evaluar(SolicitudPrestamo solicitud);
}
