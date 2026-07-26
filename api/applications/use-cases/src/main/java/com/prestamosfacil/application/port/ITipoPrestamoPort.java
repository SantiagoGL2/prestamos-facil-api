package com.prestamosfacil.application.port;

import com.prestamosfacil.model.TipoPrestamo;

import java.util.List;

public interface ITipoPrestamoPort {

    List<TipoPrestamo> listarActivos();

    TipoPrestamo buscarPorId(Long id);
}
