package com.prestamosfacil.exception;

/** Se lanza al registrar un usuario cuya combinación de tipo y número de documento ya existe. */
public class DocumentoDuplicadoException extends DomainException {

    public DocumentoDuplicadoException(String tipoDocumentoCodigo, String numeroDocumento) {
        super("Ya existe un usuario registrado con el tipo de documento " + tipoDocumentoCodigo
                + " y número de documento: " + numeroDocumento);
    }
}
