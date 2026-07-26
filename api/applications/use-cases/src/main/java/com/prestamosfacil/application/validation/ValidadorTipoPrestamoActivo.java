package com.prestamosfacil.application.validation;

import com.prestamosfacil.exception.SolicitudInvalidaException;

public class ValidadorTipoPrestamoActivo implements ValidadorSolicitud {

    @Override
    public void validar(SolicitudPrestamoContexto contexto) {
        if (!contexto.tipoPrestamo().activo()) {
            throw new SolicitudInvalidaException("El tipo de prestamo solicitado no esta activo");
        }
    }
}
