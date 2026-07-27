package com.prestamosfacil.exception;

/** Se lanza al registrar un usuario (cliente o analista) con un email que ya está en uso. */
public class EmailDuplicadoException extends DomainException {

    public EmailDuplicadoException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }
}
