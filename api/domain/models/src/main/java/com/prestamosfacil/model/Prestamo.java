package com.prestamosfacil.model;

import com.prestamosfacil.enums.EstadoPrestamo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Prestamo(Long id, Long solicitudId, BigDecimal montoAprobado, BigDecimal tasaInteresMensual,
                        BigDecimal cuotaMensual, Integer plazoMeses, LocalDateTime fechaAprobacion,
                        EstadoPrestamo estado) {
}
