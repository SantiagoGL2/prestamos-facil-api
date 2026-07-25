package com.prestamosfacil.exception;

public class TipoDocumentoNoEncontradoException extends DomainException {

    public TipoDocumentoNoEncontradoException(Long id) {
        super("Tipo de documento no encontrado con id: " + id);
    }
}
