package com.prestamosfacil.service;

import com.prestamosfacil.application.port.ITipoPrestamoPort;
import com.prestamosfacil.model.TipoPrestamo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoPrestamoService {

    private final ITipoPrestamoPort tipoPrestamoPort;

    public TipoPrestamoService(ITipoPrestamoPort tipoPrestamoPort) {
        this.tipoPrestamoPort = tipoPrestamoPort;
    }

    @Transactional(readOnly = true)
    public List<TipoPrestamo> listarActivos() {
        return tipoPrestamoPort.listarActivos();
    }

    @Transactional(readOnly = true)
    public TipoPrestamo buscarPorId(Long id) {
        return tipoPrestamoPort.buscarPorId(id);
    }
}
