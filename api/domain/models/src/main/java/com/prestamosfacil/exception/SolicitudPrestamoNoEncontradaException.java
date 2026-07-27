package com.prestamosfacil.exception;

/** Se lanza cuando se busca una solicitud de préstamo por un id que no existe. */
public class SolicitudPrestamoNoEncontradaException extends DomainException {

    public SolicitudPrestamoNoEncontradaException(Long id) {
        super("Solicitud de prestamo no encontrada con id: " + id);
    }
}
