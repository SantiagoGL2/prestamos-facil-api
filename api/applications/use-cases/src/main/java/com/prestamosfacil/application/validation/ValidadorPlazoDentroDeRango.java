package com.prestamosfacil.application.validation;

import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.TipoPrestamo;

public class ValidadorPlazoDentroDeRango implements ValidadorSolicitud {

    @Override
    public void validar(SolicitudPrestamoContexto contexto) {
        TipoPrestamo tipoPrestamo = contexto.tipoPrestamo();
        int plazoMeses = contexto.plazoMeses();

        if (plazoMeses < tipoPrestamo.plazoMinMeses() || plazoMeses > tipoPrestamo.plazoMaxMeses()) {
            throw new SolicitudInvalidaException(
                    "El plazo solicitado debe estar entre " + tipoPrestamo.plazoMinMeses() + " y "
                            + tipoPrestamo.plazoMaxMeses() + " meses");
        }
    }
}
