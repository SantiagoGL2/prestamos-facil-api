package com.prestamosfacil.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RegistrarSolicitudRequest(

        @NotNull(message = "El usuario es obligatorio")
        Long usuarioId,

        @NotNull(message = "El tipo de prestamo es obligatorio")
        Long tipoPrestamoId,

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal monto,

        @NotNull(message = "El plazo en meses es obligatorio")
        @Positive(message = "El plazo en meses debe ser mayor a cero")
        Integer plazoMeses) {
}
