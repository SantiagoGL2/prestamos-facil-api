package com.prestamosfacil.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RegistrarSolicitudRequest(

        @NotNull(message = "El tipo de prestamo es obligatorio")
        @Schema(description = "Id del tipo de préstamo solicitado (define tasa, rangos de monto/plazo y si "
                + "aplica evaluación automática)")
        Long tipoPrestamoId,

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a cero")
        @Schema(description = "Monto solicitado, debe estar dentro del rango permitido por el tipo de préstamo")
        BigDecimal monto,

        @NotNull(message = "El plazo en meses es obligatorio")
        @Positive(message = "El plazo en meses debe ser mayor a cero")
        @Schema(description = "Plazo del préstamo en meses, debe estar dentro del rango permitido por el tipo "
                + "de préstamo")
        Integer plazoMeses) {
}
