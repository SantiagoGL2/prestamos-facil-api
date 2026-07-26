package com.prestamosfacil.application.validation;

import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.model.Usuario;

import java.math.BigDecimal;

public record SolicitudPrestamoContexto(Usuario usuario, TipoPrestamo tipoPrestamo, BigDecimal monto,
                                         int plazoMeses) {
}
