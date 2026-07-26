package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ISolicitudPrestamoPort;
import com.prestamosfacil.model.SolicitudPrestamo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SolicitudPrestamoService {

    private final ISolicitudPrestamoPort solicitudPrestamoPort;

    public SolicitudPrestamoService(ISolicitudPrestamoPort solicitudPrestamoPort) {
        this.solicitudPrestamoPort = solicitudPrestamoPort;
    }

    @Transactional(rollbackFor = Exception.class)
    public SolicitudPrestamo registrarSolicitud(Long usuarioId, Long tipoPrestamoId, BigDecimal monto,
                                                 int plazoMeses) {
        return solicitudPrestamoPort.registrarSolicitud(usuarioId, tipoPrestamoId, monto, plazoMeses);
    }
}
