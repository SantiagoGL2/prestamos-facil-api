package com.prestamosfacil.application.adapter;

import com.prestamosfacil.application.port.ITipoPrestamoPort;
import com.prestamosfacil.exception.TipoPrestamoNoEncontradoException;
import com.prestamosfacil.model.TipoPrestamo;
import com.prestamosfacil.ports.ITipoPrestamoPersistencePort;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoPrestamoUseCase implements ITipoPrestamoPort {

    private final ITipoPrestamoPersistencePort tipoPrestamoPersistencePort;

    public TipoPrestamoUseCase(ITipoPrestamoPersistencePort tipoPrestamoPersistencePort) {
        this.tipoPrestamoPersistencePort = tipoPrestamoPersistencePort;
    }

    @Override
    @Cacheable(value = "tiposPrestamo")
    public List<TipoPrestamo> listarActivos() {
        return tipoPrestamoPersistencePort.listarActivos();
    }

    @Override
    public TipoPrestamo buscarPorId(Long id) {
        return tipoPrestamoPersistencePort.buscarPorId(id)
                .orElseThrow(() -> new TipoPrestamoNoEncontradoException(id));
    }
}
