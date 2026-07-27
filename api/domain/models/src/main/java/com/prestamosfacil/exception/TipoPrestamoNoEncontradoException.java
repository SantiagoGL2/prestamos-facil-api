package com.prestamosfacil.exception;

/** Se lanza cuando se referencia un tipo de préstamo por un id que no existe en el catálogo. */
public class TipoPrestamoNoEncontradoException extends DomainException {

    public TipoPrestamoNoEncontradoException(Long id) {
        super("Tipo de prestamo no encontrado con id: " + id);
    }
}
