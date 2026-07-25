package com.prestamosfacil.exception;

public class UsuarioNoEncontradoException extends DomainException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }
}
