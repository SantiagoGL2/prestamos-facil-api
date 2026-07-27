package com.prestamosfacil.application.validation;

import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.TipoPrestamo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockTipoPrestamo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorPlazoDentroDeRangoTest {

    private final ValidadorPlazoDentroDeRango validador = new ValidadorPlazoDentroDeRango();

    private final TipoPrestamo tipoPrestamo = getMockTipoPrestamo();

    @Test
    void plazoDentroDelRangoNoLanzaExcepcion() {
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 12);

        assertDoesNotThrow(() -> validador.validar(contexto));
    }

    @Test
    void plazoMenorAlMinimoLanzaExcepcion() {
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 3);

        assertThrows(SolicitudInvalidaException.class, () -> validador.validar(contexto));
    }

    @Test
    void plazoMayorAlMaximoLanzaExcepcion() {
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 72);

        assertThrows(SolicitudInvalidaException.class, () -> validador.validar(contexto));
    }
}
