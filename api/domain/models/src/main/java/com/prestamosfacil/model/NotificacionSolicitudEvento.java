package com.prestamosfacil.model;

import java.math.BigDecimal;
import java.util.List;

public record NotificacionSolicitudEvento(Long solicitudId, Long usuarioId, String usuarioNombreCompleto,
                                           String usuarioEmail, String tipoPrestamoNombre, String estadoResultante,
                                           BigDecimal montoAprobado, BigDecimal cuotaMensual,
                                           List<PlanPagoCuota> planPagos) {
}
