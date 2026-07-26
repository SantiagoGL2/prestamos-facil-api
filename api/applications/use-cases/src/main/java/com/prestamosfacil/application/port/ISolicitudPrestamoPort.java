package com.prestamosfacil.application.port;

import com.prestamosfacil.model.SolicitudPrestamo;

import java.math.BigDecimal;

public interface ISolicitudPrestamoPort {

    SolicitudPrestamo registrarSolicitud(Long usuarioId, Long tipoPrestamoId, BigDecimal monto, int plazoMeses);
}
