package com.prestamosfacil.application.strategy;

import com.prestamosfacil.model.ResultadoEvaluacionAutomatica;
import com.prestamosfacil.model.SolicitudPrestamo;
import com.prestamosfacil.ports.IEvaluacionAutomaticaPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluacionAutomaticaStrategyTest {

    @Mock
    private IEvaluacionAutomaticaPort evaluacionAutomaticaPort;

    @InjectMocks
    private EvaluacionAutomaticaStrategy strategy;

    private final SolicitudPrestamo solicitud = new SolicitudPrestamo(1L, null, null, BigDecimal.valueOf(5_000_000),
            12, null, null, null, null);

    @Test
    void resultadoAprobadoRetornaDecisionAprobar() {
        when(evaluacionAutomaticaPort.evaluar(1L)).thenReturn(new ResultadoEvaluacionAutomatica("APROBADO", null,
                null, null, null));

        DecisionEvaluacion decision = strategy.evaluar(solicitud);

        assertTrue(decision instanceof Aprobar);
        assertEquals(0, ((Aprobar) decision).montoAprobado().compareTo(BigDecimal.valueOf(5_000_000)));
    }

    @Test
    void resultadoRechazadoRetornaDecisionRechazar() {
        when(evaluacionAutomaticaPort.evaluar(1L)).thenReturn(new ResultadoEvaluacionAutomatica("RECHAZADO", null,
                null, null, null));

        DecisionEvaluacion decision = strategy.evaluar(solicitud);

        assertTrue(decision instanceof Rechazar);
    }

    @Test
    void resultadoRevisionManualRetornaDecisionRequerirRevisionManual() {
        when(evaluacionAutomaticaPort.evaluar(1L)).thenReturn(new ResultadoEvaluacionAutomatica("REVISION_MANUAL",
                null, null, null, null));

        DecisionEvaluacion decision = strategy.evaluar(solicitud);

        assertTrue(decision instanceof RequerirRevisionManual);
    }

    @Test
    void resultadoInesperadoLanzaExcepcion() {
        when(evaluacionAutomaticaPort.evaluar(1L)).thenReturn(new ResultadoEvaluacionAutomatica("ESTADO_RARO", null,
                null, null, null));

        assertThrows(IllegalStateException.class, () -> strategy.evaluar(solicitud));
    }
}
