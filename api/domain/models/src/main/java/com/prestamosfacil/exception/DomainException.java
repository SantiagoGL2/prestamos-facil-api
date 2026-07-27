package com.prestamosfacil.exception;

/**
 * Base de toda excepción de negocio del dominio. El {@code ControllerAdvisor} mapea cada
 * subtipo concreto a un código HTTP específico según el tipo de violación que representa.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
