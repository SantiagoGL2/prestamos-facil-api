package com.prestamosfacil.exception;

public class EmailDuplicadoException extends DomainException {

    public EmailDuplicadoException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }
}
