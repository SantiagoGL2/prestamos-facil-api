package com.prestamosfacil.ports;

import com.prestamosfacil.model.TipoPrestamo;

import java.util.List;
import java.util.Optional;

public interface ITipoPrestamoPersistencePort {

    Optional<TipoPrestamo> buscarPorId(Long id);

    List<TipoPrestamo> listarActivos();
}
