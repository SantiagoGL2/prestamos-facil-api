package com.prestamosfacil.exception;

public class SolicitudPrestamoNoEncontradaException extends DomainException {

    public SolicitudPrestamoNoEncontradaException(Long id) {
        super("Solicitud de prestamo no encontrada con id: " + id);
    }
}
