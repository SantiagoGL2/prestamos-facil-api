package com.prestamosfacil.exception;

public class UsuarioNoEncontradoException extends DomainException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UsuarioNoEncontradoException(Long tipoDocumentoId, String numeroDocumento) {
        super("Usuario no encontrado con tipo de documento " + tipoDocumentoId
                + " y numero de documento: " + numeroDocumento);
    }
}
