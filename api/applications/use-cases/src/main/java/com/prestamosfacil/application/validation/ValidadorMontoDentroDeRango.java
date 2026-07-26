package com.prestamosfacil.application.validation;

import com.prestamosfacil.exception.SolicitudInvalidaException;
import com.prestamosfacil.model.TipoPrestamo;

import java.math.BigDecimal;

public class ValidadorMontoDentroDeRango implements ValidadorSolicitud {

    @Override
    public void validar(SolicitudPrestamoContexto contexto) {
        TipoPrestamo tipoPrestamo = contexto.tipoPrestamo();
        BigDecimal monto = contexto.monto();

        if (monto.compareTo(tipoPrestamo.montoMin()) < 0 || monto.compareTo(tipoPrestamo.montoMax()) > 0) {
            throw new SolicitudInvalidaException(
                    "El monto solicitado debe estar entre " + tipoPrestamo.montoMin() + " y " + tipoPrestamo.montoMax());
        }
    }
}
