package com.prestamosfacil.ports;

import com.prestamosfacil.model.Prestamo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IPrestamoPersistencePort {

    Prestamo guardar(Prestamo prestamo);

    Optional<Prestamo> buscarPorSolicitudId(Long solicitudId);

    List<Prestamo> listarActivos();

    BigDecimal sumarMontoTotalAprobado();
}
