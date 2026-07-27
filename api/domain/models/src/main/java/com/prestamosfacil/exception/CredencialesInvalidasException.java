package com.prestamosfacil.exception;

/** Se lanza al hacer login cuando el email no existe o la contraseña no coincide (mismo mensaje en ambos casos, para no revelar si el email está registrado). */
public class CredencialesInvalidasException extends DomainException {

    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
