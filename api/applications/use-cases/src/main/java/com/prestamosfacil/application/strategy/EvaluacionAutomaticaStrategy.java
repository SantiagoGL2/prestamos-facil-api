package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.ResultadoEvaluacionAutomatica;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.ports.IEvaluacionAutomaticaPort;

import org.springframework.stereotype.Component;

/**
 * Estrategia para tipos de préstamo con validación automática habilitada. No reimplementa
 * ninguna regla de negocio en Java — delega la decisión completa (aprobar, rechazar o derivar
 * a revisión manual) en el Stored Procedure vía {@link IEvaluacionAutomaticaPort}, y solo
 * traduce el {@code estadoResultante} que este devuelve a una {@link DecisionEvaluacion}.
 */
@Component
public class EvaluacionAutomaticaStrategy implements EvaluacionPrestamoStrategy {

    private final IEvaluacionAutomaticaPort evaluacionAutomaticaPort;

    public EvaluacionAutomaticaStrategy(IEvaluacionAutomaticaPort evaluacionAutomaticaPort) {
        this.evaluacionAutomaticaPort = evaluacionAutomaticaPort;
    }

    @Override
    public DecisionEvaluacion evaluar(SolicitudPrestamo solicitud) {
        ResultadoEvaluacionAutomatica resultado = evaluacionAutomaticaPort.evaluar(solicitud.id());

        return switch (resultado.estadoResultante()) {
            case "APROBADO" -> new Aprobar(solicitud.monto());
            case "RECHAZADO" -> new Rechazar("La evaluacion automatica rechazo la solicitud");
            case "REVISION_MANUAL" -> new RequerirRevisionManual();
            default -> throw new IllegalStateException(
                    "Estado resultante de evaluacion automatica desconocido: " + resultado.estadoResultante());
        };
    }
}
