package com.prestamosfacil.application.validation;

import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.TipoPrestamo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockTipoPrestamo;
import static com.prestamosfacil.application.mocks.PrestamoFacilMocks.getMockTipoPrestamoError;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorTipoPrestamoActivoTest {

    private final ValidadorTipoPrestamoActivo validador = new ValidadorTipoPrestamoActivo();

    @Test
    void tipoPrestamoActivoNoLanzaExcepcion() {
        TipoPrestamo tipoPrestamo = getMockTipoPrestamo();
        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 12);

        assertDoesNotThrow(() -> validador.validar(contexto));
    }

    @Test
    void tipoPrestamoInactivoLanzaExcepcion() {
        TipoPrestamo tipoPrestamo = getMockTipoPrestamoError();

        SolicitudPrestamoContexto contexto = new SolicitudPrestamoContexto(null, tipoPrestamo,
                BigDecimal.valueOf(5_000_000), 12);

        assertThrows(SolicitudInvalidaException.class, () -> validador.validar(contexto));
    }
}
