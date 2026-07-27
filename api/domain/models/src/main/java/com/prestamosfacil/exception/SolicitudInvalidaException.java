package com.prestamosfacil.exception;

/**
 * Excepción genérica para violaciones de reglas de negocio que no tienen su propio tipo
 * dedicado: monto/plazo fuera de rango, tipo de préstamo inactivo, fechas de filtro inválidas,
 * transición de estado no permitida, analista sin el rol correcto, etc. El mensaje siempre
 * describe la regla puntual incumplida.
 */
public class SolicitudInvalidaException extends DomainException {

    public SolicitudInvalidaException(String message) {
        super(message);
    }
}
