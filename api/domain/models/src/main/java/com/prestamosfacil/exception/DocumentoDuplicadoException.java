package com.prestamosfacil.exception;

public class DocumentoDuplicadoException extends DomainException {

    public DocumentoDuplicadoException(String tipoDocumentoCodigo, String numeroDocumento) {
        super("Ya existe un usuario registrado con el tipo de documento " + tipoDocumentoCodigo
                + " y número de documento: " + numeroDocumento);
    }
}
