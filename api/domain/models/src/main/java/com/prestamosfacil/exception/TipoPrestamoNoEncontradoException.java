package com.prestamosfacil.exception;

public class TipoPrestamoNoEncontradoException extends DomainException {

    public TipoPrestamoNoEncontradoException(Long id) {
        super("Tipo de prestamo no encontrado con id: " + id);
    }
}
