package com.prestamosfacil.exception;

/**
 * Se lanza cuando no se encuentra un usuario, ya sea por id (por ejemplo, al validar el
 * analista de una solicitud) o por la combinación tipo + número de documento.
 */
public class UsuarioNoEncontradoException extends DomainException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UsuarioNoEncontradoException(Long tipoDocumentoId, String numeroDocumento) {
        super("Usuario no encontrado con tipo de documento " + tipoDocumentoId
                + " y numero de documento: " + numeroDocumento);
    }
}
