package com.prestamosfacil.application.validation;

import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.TipoPrestamo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockTipoPrestamo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorMontoDentroDeRangoTest {

    private final ValidadorMontoDentroDeRango validador = new ValidadorMontoDentroDeRango();

    private final TipoPrestamo tipoPrestamo = getMockTipoPrestamo();

    @Test
    void montoDentroDelRangoNoLanzaExcepcion() {
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 12);

        assertDoesNotThrow(() -> validador.validar(contexto));
    }

    @Test
    void montoMenorAlMinimoLanzaExcepcion() {
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(500_000), 12);

        assertThrows(SolicitudInvalidaException.class, () -> validador.validar(contexto));
    }

    @Test
    void montoMayorAlMaximoLanzaExcepcion() {
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(60_000_000), 12);

        assertThrows(SolicitudInvalidaException.class, () -> validador.validar(contexto));
    }
}
