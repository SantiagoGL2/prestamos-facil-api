package com.prestamosfacil.exception;

/** Se lanza cuando se referencia un tipo de documento por un id que no existe en el catálogo. */
public class TipoDocumentoNoEncontradoException extends DomainException {

    public TipoDocumentoNoEncontradoException(Long id) {
        super("Tipo de documento no encontrado con id: " + id);
    }
}
