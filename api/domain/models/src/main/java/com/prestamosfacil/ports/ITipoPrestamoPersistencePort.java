package com.prestamosfacil.ports;

import com.prestamosfacil.model.TipoPrestamo;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para el catálogo de tipos de préstamo (tasa, rangos de monto/plazo y
 * si tiene habilitada la validación automática). Al igual que tipo de documento, es un catálogo
 * de solo lectura desde la API.
 */
public interface ITipoPrestamoPersistencePort {

    Optional<TipoPrestamo> buscarPorId(Long id);

    List<TipoPrestamo> listarActivos();
}
