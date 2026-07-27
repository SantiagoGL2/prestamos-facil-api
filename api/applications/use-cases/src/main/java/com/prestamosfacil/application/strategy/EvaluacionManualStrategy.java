package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.SolicitudPrestamo;

import org.springframework.stereotype.Component;

/**
 * Estrategia para tipos de préstamo sin validación automática. Es intencionalmente un no-op:
 * siempre retorna {@link RequerirRevisionManual} sin evaluar nada, porque el negocio ya decidió
 * de antemano que ese tipo de préstamo requiere un analista. Se mantiene como implementación
 * explícita del patrón Strategy (en vez de un {@code if} disperso) para que agregar un tercer
 * comportamiento en el futuro no obligue a tocar {@code SolicitudPrestamoUseCase}.
 */
@Component
public class EvaluacionManualStrategy implements EvaluacionPrestamoStrategy {

    @Override
    public DecisionEvaluacion evaluar(SolicitudPrestamo solicitud) {
        return new RequerirRevisionManual();
    }
}
